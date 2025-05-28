package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class BrandFormDto {
    private Integer id;
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;

    public void setName(String name) {
        if (name == null || name.trim().isBlank())
            this.name = null;
        else
            this.name = name.trim();
    }

    public void setWebsiteUrl(String websiteUrl) {
        if (websiteUrl == null || websiteUrl.trim().isBlank())
            this.websiteUrl = null;
        else
            this.websiteUrl = websiteUrl.trim();
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        if (countryOfOrigin == null || countryOfOrigin.trim().isBlank())
            this.countryOfOrigin = null;
        else
            this.countryOfOrigin = countryOfOrigin.trim();
    }
}
