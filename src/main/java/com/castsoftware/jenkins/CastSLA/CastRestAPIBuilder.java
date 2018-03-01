package com.castsoftware.jenkins.CastSLA;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.castsoftware.restapi.CastRest;
import com.castsoftware.restapi.RestException;
import com.castsoftware.restapi.pojo.Aad;
import com.castsoftware.restapi.pojo.Application;
import com.castsoftware.restapi.pojo.ApplicationResult;
import com.castsoftware.restapi.pojo.Metric;
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;

public class CastRestAPIBuilder extends Builder {

	private final String restUrl;
	private final String credentialsId;
	private final String aad;
	private final String appId;
	private final String metricFromList;
	private final String metricIds;
	private final boolean failJob;
	private final boolean debugMode;

	private List<HealthFactorConditionParam> healthFactorConditionParams;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public CastRestAPIBuilder(String restUrl, String credentialsId, String aad, String appId, String metricFromList,
			String metricIds, List<HealthFactorConditionParam> healthFactorConditionParams, boolean failJob, boolean debugMode) {
		this.restUrl = restUrl;
		this.credentialsId = credentialsId;
		this.aad = aad;
		this.appId = appId;
		this.metricFromList = metricFromList;
		this.metricIds = metricIds;

		this.healthFactorConditionParams = healthFactorConditionParams;
		this.failJob = failJob;
		this.debugMode = debugMode;
	}

	public String getRestUrl() {
		return restUrl;
	}

	public String getCredentialsId() {
		return credentialsId;
	}

	public String getAad() {
		return aad;
	}

	public String getAppId() {
		return appId;
	}

	public Integer getAppIdAsInteger() {
		return Integer.parseInt(appId);
	}

	public List<HealthFactorConditionParam> getHealthFactorConditionParams() {
		return healthFactorConditionParams;
	}

	public boolean getFailJob() {
		return failJob;
	}

	public String getMetricFromList() {
		return metricFromList;
	}

	public String getMetricIds() {
		return metricIds;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException {
		boolean validateBuild = true;
		CastRest.listener=listener;
		CastRest.debugMode=debugMode;
		
		listener.getLogger().println(String.format("Cast Rest API Url: %s", restUrl));
		StandardUsernamePasswordCredentials credentials = DescriptorImpl.lookupCredentials(build.getProject(),
				credentialsId, restUrl);
		if (credentials == null) {
			listener.getLogger().println("No credentials defined!");
			return false;
		}

		listener.getLogger().println(String.format("Login: %s", credentials.getUsername()));
		listener.getLogger().println(String.format("Password: ******"));
		listener.getLogger().println(String.format("AAD: %s", aad));
		listener.getLogger().println(String.format("Application: %s", appId));

		try {			
			int LastSnapshotId = CastRest.getLastSnapshotId(restUrl, credentials.getUsername(),
					credentials.getPassword().getPlainText(), aad, getAppIdAsInteger(), false);
			
			int LastProdSnapshotId = CastRest.getLastSnapshotId(restUrl, credentials.getUsername(),
					credentials.getPassword().getPlainText(), aad, getAppIdAsInteger(), true);

			if (healthFactorConditionParams != null)
				for (HealthFactorConditionParam mcp : healthFactorConditionParams) {
					listener.getLogger().println(String.format("Health Factor Condition: %s", mcp.toString()));
					double metricValue = CastRest.getMetric(restUrl, credentials.getUsername(),
							credentials.getPassword().getPlainText(), aad, getAppIdAsInteger(), LastSnapshotId,
							mcp.getMetricGroup(), mcp.getMetricId(), mcp.getMetricField());

					String conditionMessage = "";
					// ">",">=","=","!=","<=","<"
					if (mcp.getOperatorName().equals(">")) {
						if (!(metricValue > mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					} else if (mcp.getOperatorName().equals(">=")) {
						if (!(metricValue >= mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					} else if (mcp.getOperatorName().equals("=")) {
						if (!(metricValue == mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					} else if (mcp.getOperatorName().equals("!=")) {
						if (!(metricValue != mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					} else if (mcp.getOperatorName().equals("<=")) {
						if (!(metricValue <= mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					} else if (mcp.getOperatorName().equals("<")) {
						if (!(metricValue < mcp.getValueAsDouble())) {
							conditionMessage = "--> FAILED";
							validateBuild = false;
						}
					}  else if (mcp.getOperatorName().equals("Change")) {
						if (LastProdSnapshotId>0)
						{
							double prodMetricValue = CastRest.getMetric(restUrl, credentials.getUsername(),
									credentials.getPassword().getPlainText(), aad, getAppIdAsInteger(), LastProdSnapshotId,
									mcp.getMetricGroup(), mcp.getMetricId(), mcp.getMetricField());
	
							if (metricValue + mcp.getValueAsDouble() < prodMetricValue)
							{
								conditionMessage = "--> FAILED";
								validateBuild = false;
							}
						} else {
							listener.getLogger().println("No Production snapshot found, skiping Change comparison");
						}
					}
					listener.getLogger().println(String.format(" - %.2f %s", metricValue, conditionMessage));
				}

			int responseStatus;
			if (metricIds != null)
				for (String metricIdStr : metricIds.split(";")) {
					int metricId;
					try {
						metricId = Integer.parseInt(metricIdStr);
					} catch (NumberFormatException e) {
						metricId = -1;
					}

					if (metricId == -1)
						listener.getLogger().println(String.format("%s is not a valid metric Id\n", metricIdStr));
					else {
						List<Metric> metrics = new ArrayList<Metric>();
						responseStatus = CastRest.getMetricForApp(restUrl, credentials.getUsername(),
								credentials.getPassword().getPlainText(), aad, getAppIdAsInteger(), metricId, true,
								metrics);

						if (responseStatus == 200) {
							if (metrics.size() > 0 && metrics.get(0).applicationResults.size() > 0) {
								try {
									if (metrics.get(0).applicationResults
											.get(0).result.evolutionSummary.addedViolations > 0)
										validateBuild = false;
									listener.getLogger()
											.println(String.format("%d: %s (New Violations: %d)\n", metricId,
													metrics.get(0).applicationResults.get(0).reference.name,
													metrics.get(0).applicationResults
															.get(0).result.evolutionSummary.addedViolations));
								} catch (NullPointerException e) {
									listener.getLogger().println(
											"JSON stream is missing 'evolutionSummary' information. CAST Rest API 8.2.x or superior needed.");
								}
							} else
								listener.getLogger().println(
										String.format("Metric Id %d is invalid for this application\n", metricId));
						} else
							listener.getLogger().println(
									String.format("Unexpected response code from Rest API: %d\n", responseStatus));
					}
				}
		} catch (RestException e) {
			listener.getLogger().println(String.format("%s", e.getMessage()));
			validateBuild = false;
		}
		return validateBuild || !failJob;
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor for {@link CastRestAPIBuilder}. Used as a singleton. The class
	 * is marked as public so that it can be accessed from views.
	 *
	 * <p>
	 * See
	 * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension // This indicates to Jenkins that this is an implementation of an
				// extension point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 *
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */
		// private boolean useFrench;

		/**
		 * In order to load the persisted global configuration, you have to call
		 * load() in the constructor.
		 */
		public DescriptorImpl() {
			load();
		}

		@SuppressWarnings("rawtypes")
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "Cast SLA";
		}

		public FormValidation doTestConnection(@AncestorInPath Item context,
				@QueryParameter("restUrl") final String restUrl,
				@QueryParameter("credentialsId") final String credentialsId) throws IOException, ServletException {
			try {
				StandardUsernamePasswordCredentials credentials = lookupCredentials(context, credentialsId, restUrl);

				int responseStatus = CastRest.testConnection(restUrl, credentials.getUsername(),
						credentials.getPassword().getPlainText());

				switch (responseStatus) {
				case -200:
					return FormValidation.error("Connected but that address does not host a Cast REST API!");
				case 200:
					return FormValidation.ok("Success");
				case 470:
					return FormValidation.error("Invalid Login/Password");
				default:
					return FormValidation.error(String.format("Unknown Response Code - %d", responseStatus));
				}
			} catch (Exception e) {
				return FormValidation.error("Exception: " + e.getMessage());
			}
		}

		private static StandardUsernamePasswordCredentials lookupCredentials(Item context, String credentialsId,
				String repoURL) {
			List<StandardUsernamePasswordCredentials> matchingCredentials = CredentialsProvider.lookupCredentials(
					StandardUsernamePasswordCredentials.class, context, ACL.SYSTEM,
					URIRequirementBuilder.fromUri(repoURL).build());
			return CredentialsMatchers.firstOrNull(matchingCredentials, CredentialsMatchers.withId(credentialsId));

		}

		public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item context, @QueryParameter String remote) {
			if (context == null || !context.hasPermission(Item.CONFIGURE)) {
				return new ListBoxModel();
			}
			return fillCredentialsIdItems(context, remote);
		}

		public ListBoxModel fillCredentialsIdItems(@Nonnull Item context, String remote) {
			List<DomainRequirement> domainRequirements;
			if (remote == null) {
				domainRequirements = Collections.<DomainRequirement>emptyList();
			} else {
				domainRequirements = URIRequirementBuilder.fromUri(remote.trim()).build();
			}
			return new StandardListBoxModel().withEmptySelection().withMatching(
					CredentialsMatchers
							.anyOf(CredentialsMatchers.instanceOf(StandardUsernamePasswordCredentials.class)),
					CredentialsProvider.lookupCredentials(StandardCredentials.class, context, ACL.SYSTEM,
							domainRequirements));
		}

		public ListBoxModel doFillAadItems(@AncestorInPath Item context,
				@QueryParameter("restUrl") final String restUrl,
				@QueryParameter("credentialsId") final String credentialsId) {
			ListBoxModel m = new ListBoxModel();

			Collection<Aad> Aads = new ArrayList<Aad>();

			StandardUsernamePasswordCredentials credentials = lookupCredentials(context, credentialsId, restUrl);

			int responseStatus = CastRest.listAads(restUrl, credentials.getUsername(),
					credentials.getPassword().getPlainText(), Aads);

			if (responseStatus == 200) {
				for (Aad aad : Aads) {
					m.add(aad.getHref());
				}
			}
			return m;
		}

		public ListBoxModel doFillAppIdItems(@AncestorInPath Item context,
				@QueryParameter("restUrl") final String restUrl,
				@QueryParameter("credentialsId") final String credentialsId, @QueryParameter("aad") String aad) {
			ListBoxModel m = new ListBoxModel();

			Collection<Application> apps = new ArrayList<Application>();
			StandardUsernamePasswordCredentials credentials = lookupCredentials(context, credentialsId, restUrl);

			int responseStatus = CastRest.listApplications(restUrl, credentials.getUsername(),
					credentials.getPassword().getPlainText(), aad, apps);

			if (responseStatus == 200) {
				for (Application app : apps) {
					m.add(app.name, app.href.substring(app.href.lastIndexOf("/") + 1));
				}
			}
			return m;
		}

		public ListBoxModel doFillMetricFromListItems(@AncestorInPath Item context,
				@QueryParameter("restUrl") final String restUrl,
				@QueryParameter("credentialsId") final String credentialsId, @QueryParameter("aad") final String aad,
				@QueryParameter("appId") final int appId) {
			ListBoxModel m = new ListBoxModel();

			List<Metric> metrics = new ArrayList<Metric>();
			StandardUsernamePasswordCredentials credentials = lookupCredentials(context, credentialsId, restUrl);

			int responseStatus = CastRest.listMetricsForApp(restUrl, credentials.getUsername(),
					credentials.getPassword().getPlainText(), aad, appId, metrics);

			if (responseStatus == 200) {
				if (metrics.size() > 0) {
					Collections.sort(metrics.get(0).applicationResults, new Comparator<ApplicationResult>() {
						public int compare(ApplicationResult ar1, ApplicationResult ar2) {
							return ar1.reference.name.compareTo(ar2.reference.name);
						}
					});
					for (ApplicationResult metric : metrics.get(0).applicationResults) {
						m.add(String.format("%s (#%s)", metric.reference.name, metric.reference.key),
								metric.reference.key);
					}
				}
			}
			return m;
		}

		public FormValidation doCheckMetrics(@AncestorInPath Item context,
				@QueryParameter("restUrl") final String restUrl,
				@QueryParameter("credentialsId") final String credentialsId, @QueryParameter("aad") final String aad,
				@QueryParameter("appId") final int appId, @QueryParameter("metricIds") final String metricIds)
				throws IOException, ServletException {
			StringBuilder validationMessage = new StringBuilder();
			try {
				validationMessage.append("Look up credentials...\n");
				StandardUsernamePasswordCredentials credentials = lookupCredentials(context, credentialsId, restUrl);

				int responseStatus;

				boolean inError = false;

				for (String metricIdStr : metricIds.split(";")) {
					int metricId;
					try {
						metricId = Integer.parseInt(metricIdStr);
					} catch (NumberFormatException e) {
						metricId = -1;
					}

					if (metricId == -1) {
						validationMessage.append(String.format("%s is not a valid metric Id\n", metricIdStr));
						inError = true;
					} else {
						validationMessage.append(String.format("Checking metric %d...", metricId));
						List<Metric> metrics = new ArrayList<Metric>();
						responseStatus = CastRest.getMetricForApp(restUrl, credentials.getUsername(),
								credentials.getPassword().getPlainText(), aad, appId, metricId, true, metrics);
						if (responseStatus == 200) {
							if (metrics.size() > 0 && metrics.get(0).applicationResults.size() > 0) {
								try {
									validationMessage.append(String.format("%d: %s (New Violations: %d)\n", metricId,
											metrics.get(0).applicationResults.get(0).reference.name,
											metrics.get(0).applicationResults
													.get(0).result.evolutionSummary.addedViolations));
								} catch (NullPointerException e) {
									validationMessage.append(
											"JSON stream is missing 'evolutionSummary' information. CAST Rest API 8.2.x or superior needed.\n");
									inError = true;
								}
							} else {
								validationMessage.append(
										String.format("Metric Id %d is invalid for this application\n", metricId));
								inError = true;
							}
						} else {
							validationMessage.append(
									String.format("Unexpected response code from Rest API: %d\n", responseStatus));
							inError = true;
						}
					}
				}

				if (inError)
					return FormValidation.error(validationMessage.toString());
				else
					return FormValidation.ok(validationMessage.toString());
			} catch (Exception e) {
				return FormValidation
						.error(String.format("Exception: %s\n%s", e.getMessage(), validationMessage.toString()));
			}
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			// useFrench = formData.getBoolean("useFrench");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

	}
}
