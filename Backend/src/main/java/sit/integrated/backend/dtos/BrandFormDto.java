package sit.integrated.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class BrandFormDto {
    private Integer id;

    @NotBlank
    @Size(max = 30)
    private String name;

    @URL
    @Size(max = 40)
    private String websiteUrl;

    @Size(max = 80)
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
