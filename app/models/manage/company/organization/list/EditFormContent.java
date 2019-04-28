package models.manage.company.organization.list;

import common.data.validation.groups.Create;
import common.data.validation.groups.Delete;
import common.data.validation.groups.Update;
import play.data.validation.Constraints;

public class EditFormContent {

	// Play
	@Constraints.Required
	private long companyId;

	// Play
	private Long organizationId;

	// Play
	@Constraints.Required(groups = { Update.class, Delete.class })
	private String organizationVersion;

	// Play
	@Constraints.Required(groups = { Update.class, Delete.class })
	private long id;

	// Play
	@Constraints.Required(groups = { Create.class, Update.class })
	private String name;

	// Play
	private String localName;

	// Play
	@Constraints.Required(groups = { Update.class, Delete.class })
	private Long version;

	public long getCompanyId() {

		return companyId;
	}

	public void setCompanyId(long companyId) {

		this.companyId = companyId;
	}

	public Long getOrganizationId() {

		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {

		this.organizationId = organizationId;
	}

	public String getOrganizationVersion() {

		return organizationVersion;
	}

	public void setOrganizationVersion(String organizationVersion) {

		this.organizationVersion = organizationVersion;
	}

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getLocalName() {

		return localName;
	}

	public void setLocalName(String localName) {

		this.localName = localName;
	}

	public Long getVersion() {

		return version;
	}

	public void setVersion(Long version) {

		this.version = version;
	}
}
