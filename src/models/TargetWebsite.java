package models;

public class TargetWebsite
{
    private Integer Id;
    private String Url;

    public TargetWebsite() {

    }
    public TargetWebsite(String url){
        this.Url = url;
    }
    public TargetWebsite(Integer Id,String Url)
    {
        this.Id = Id;
        this.Url = Url;
    }

    public Integer getId()
    {
        return Id;
    }
    public void setId(Integer Id)
    {
        this.Id = Id;
    }
    public String getUrl()
    {
        return Url;
    }
    public void setUrl(String Url)
    {
        this.Url = Url;
    }
}