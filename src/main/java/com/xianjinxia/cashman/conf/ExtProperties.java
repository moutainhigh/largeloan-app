/**
 * 
 */
package com.xianjinxia.cashman.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author liujun
 * ext配置文件读取
 */
@Configuration
@ConfigurationProperties(prefix = "ext",ignoreInvalidFields = false)
public class ExtProperties {
	
	private final Swagger swagger = new Swagger();

	private final ActiveMqConfiguration activeMqConfiguration = new ActiveMqConfiguration();
    

    private final RedisCluster redisCluster = new RedisCluster();

	private final LoanConfig loanConfig = new LoanConfig();

	private final SmsConfig smsConfig=new SmsConfig();
	
	private final OldCashmanServerAddressConfig oldCashmanServerAddressConfig = new OldCashmanServerAddressConfig();

	private final SdkInitConfig sdkInitConfig = new SdkInitConfig();

	private final MoneyInterestCalc moneyInterestCalc=new MoneyInterestCalc();

	private final ContractRelatedConfig contractRelatedConfig = new ContractRelatedConfig();
    
    public Swagger getSwagger() {
    	return swagger;
    }
    
    public ActiveMqConfiguration getActiveMqConfiguration() {
    	return activeMqConfiguration;
    }
    

    public RedisCluster getRedisCluster() {
    	return redisCluster;
    }

	public LoanConfig getLoanConfig() {
		return loanConfig;
	}

	public SmsConfig getSmsConfig() {
        return smsConfig;
    }

    public MoneyInterestCalc getMoneyInterestCalc() {
        return moneyInterestCalc;
    }

    public OldCashmanServerAddressConfig getOldCashmanServerAddressConfig() {
		return oldCashmanServerAddressConfig;
	}

	public SdkInitConfig getSdkInitConfig() {
		return sdkInitConfig;
	}

	public ContractRelatedConfig getContractRelatedConfig() {
		return contractRelatedConfig;
	}

	public static class Swagger {

        private String title = "uninoty API";
        
		private String description = "uninoty API documentation";

        private String version = "0.0.1";

        private String termsOfServiceUrl;

        private String contactName;

        private String contactUrl;

        private String contactEmail;

        private String license;

        private String licenseUrl;

        private Boolean enabled;

        public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactUrl() {
			return contactUrl;
		}

		public void setContactUrl(String contactUrl) {
			this.contactUrl = contactUrl;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}


    }
    
    public static class ActiveMqConfiguration {

        private String brokerUrl;
        
        private String queueTableName;
        
        private String queueName;
        
        private Integer queueMaxCount;
        
        private Integer isCreateTable;
        
        public String getBrokerUrl() {
			return brokerUrl;
		}

		public void setBrokerUrl(String brokerUrl) {
			this.brokerUrl = brokerUrl;
		}

		public String getQueueTableName() {
			return queueTableName;
		}

		public void setQueueTableName(String queueTableName) {
			this.queueTableName = queueTableName;
		}

		public String getQueueName() {
			return queueName;
		}

		public void setQueueName(String queueName) {
			this.queueName = queueName;
		}

		public Integer getQueueMaxCount() {
			return queueMaxCount;
		}

		public void setQueueMaxCount(Integer queueMaxCount) {
			this.queueMaxCount = queueMaxCount;
		}

		public Integer getIsCreateTable() {
			return isCreateTable;
		}

		public void setIsCreateTable(Integer isCreateTable) {
			this.isCreateTable = isCreateTable;
		}

    }
    

    public static class RedisCluster {

    	private int		expireSeconds;
    	private String	clusterNodes;
    	private int		commandTimeout;
    	private String	password;
    	private int		timeout;
    	private int		maxRedirections;
    	private int		tryNum;

    	public int getTimeout() {
    		return timeout;
    	}

    	public void setTimeout(int timeout) {
    		this.timeout = timeout;
    	}

    	public int getMaxRedirections() {
    		return maxRedirections;
    	}

    	public void setMaxRedirections(int maxRedirections) {
    		this.maxRedirections = maxRedirections;
    	}

    	public int getTryNum() {
    		return tryNum;
    	}

    	public void setTryNum(int tryNum) {
    		this.tryNum = tryNum;
    	}

    	public String getPassword() {
    		return password;
    	}

    	public void setPassword(String password) {
    		this.password = password;
    	}

    	public int getExpireSeconds() {
    		return expireSeconds;
    	}

    	public void setExpireSeconds(int expireSeconds) {
    		this.expireSeconds = expireSeconds;
    	}

    	public String getClusterNodes() {
    		return clusterNodes;
    	}

    	public void setClusterNodes(String clusterNodes) {
    		this.clusterNodes = clusterNodes;
    	}

    	public int getCommandTimeout() {
    		return commandTimeout;
    	}

    	public void setCommandTimeout(int commandTimeout) {
    		this.commandTimeout = commandTimeout;
    	}

    }


	public static class LoanConfig {

		private String renewalFee;

		public String getRenewalFee() {
			return renewalFee;
		}

		public void setRenewalFee(String renewalFee) {
			this.renewalFee = renewalFee;
		}
		
	}

	public static class SmsConfig{
    	private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	public static class MoneyInterestCalc{
    	private String bigAmount;

		public String getBigAmount() {
			return bigAmount;
		}

		public void setBigAmount(String bigAmount) {
			this.bigAmount = bigAmount;
		}
	}
	
	public static class OldCashmanServerAddressConfig{
		
		private String serverAddress;

		public String getServerAddress() {
			return serverAddress;
		}

		public void setServerAddress(String serverAddress) {
			this.serverAddress = serverAddress;
		}
	}
	public static class SdkInitConfig {
    	private String projectAppId;
    	private String projectAppSecret;
    	private String projectUrl;

		public String getProjectAppId() {
			return projectAppId;
		}

		public void setProjectAppId(String projectAppId) {
			this.projectAppId = projectAppId;
		}

		public String getProjectAppSecret() {
			return projectAppSecret;
		}

		public void setProjectAppSecret(String projectAppSecret) {
			this.projectAppSecret = projectAppSecret;
		}

		public String getProjectUrl() {
			return projectUrl;
		}

		public void setProjectUrl(String projectUrl) {
			this.projectUrl = projectUrl;
		}
	}

	public static class ContractRelatedConfig{
		private String contractUploadFolder;
		private String cloudSpaceName;
		private Long ossUrlExpire;

		public String getContractUploadFolder() {
			return contractUploadFolder;
		}

		public void setContractUploadFolder(String contractUploadFolder) {
			this.contractUploadFolder = contractUploadFolder;
		}

		public String getCloudSpaceName() {
			return cloudSpaceName;
		}

		public void setCloudSpaceName(String cloudSpaceName) {
			this.cloudSpaceName = cloudSpaceName;
		}

		public Long getOssUrlExpire() {
			return ossUrlExpire;
		}

		public void setOssUrlExpire(Long ossUrlExpire) {
			this.ossUrlExpire = ossUrlExpire;
		}


	}
}
