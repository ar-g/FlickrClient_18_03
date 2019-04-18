package e.ar_g.flickrclient_18_03.model;

import com.google.gson.annotations.SerializedName;

public class PhotoItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("server")
	private String server;

	@SerializedName("ispublic")
	private int isPublic;

	@SerializedName("isfriend")
	private int isFriend;

	@SerializedName("farm")
	private int farm;

	@SerializedName("id")
	private String id;

	@SerializedName("secret")
	private String secret;

	@SerializedName("title")
	private String title;

	@SerializedName("isfamily")
	private int isFamily;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setServer(String server){
		this.server = server;
	}

	public String getServer(){
		return server;
	}

	public void setIspublic(int ispublic){
		this.isPublic = ispublic;
	}

	public int getIspublic(){
		return isPublic;
	}

	public void setIsfriend(int isfriend){
		this.isFriend = isfriend;
	}

	public int getIsfriend(){
		return isFriend;
	}

	public void setFarm(int farm){
		this.farm = farm;
	}

	public int getFarm(){
		return farm;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSecret(String secret){
		this.secret = secret;
	}

	public String getSecret(){
		return secret;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsfamily(int isfamily){
		this.isFamily = isfamily;
	}

	public int getIsfamily(){
		return isFamily;
	}

	@Override
 	public String toString(){
		return 
			"PhotoItem{" + 
			"owner = '" + owner + '\'' + 
			",server = '" + server + '\'' + 
			",ispublic = '" + isPublic + '\'' +
			",isfriend = '" + isFriend + '\'' +
			",farm = '" + farm + '\'' + 
			",id = '" + id + '\'' + 
			",secret = '" + secret + '\'' + 
			",title = '" + title + '\'' + 
			",isfamily = '" + isFamily + '\'' +
			"}";
		}
}