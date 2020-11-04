package it.attsd.deepsky.pojo.deepskyobject;

public class DeepSkyObjectPojo {
	private long id;
	
	private String name;
	
	private long constellationId;
	
	private long deepSkyObjectTypeId;

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

	public long getConstellationId() {
		return constellationId;
	}

	public void setConstellationId(long constellationId) {
		this.constellationId = constellationId;
	}

	public long getDeepSkyObjectTypeId() {
		return deepSkyObjectTypeId;
	}

	public void setDeepSkyObjectTypeId(long deepSkyObjectTypeId) {
		this.deepSkyObjectTypeId = deepSkyObjectTypeId;
	}
	
}