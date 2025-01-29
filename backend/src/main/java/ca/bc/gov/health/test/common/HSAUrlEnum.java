package ca.bc.gov.health.test.common;

public enum HSAUrlEnum {

    PROVIDER_ID("PROVIDER_ID", "/HSA-web/fhir-rs/Practitioner/?identifier=");

    private final String identifierType;
    private final String URL;

    HSAUrlEnum(String identifierType, String URL) {
        this.identifierType = identifierType;
        this.URL = URL;
    }

    public String identifierType() {
        return identifierType;
    }

    public String url() {
        return URL;
    }

    public static HSAUrlEnum findByIdentifierType(String idType) {
        HSAUrlEnum result = null;
        for (HSAUrlEnum ns : values()) {
            if (ns.identifierType.equalsIgnoreCase(idType)) {
                result = ns;
                break;
            }
        }
        return result;
    }

    public static HSAUrlEnum findByUrl(String URL) {
        HSAUrlEnum result = null;
        for (HSAUrlEnum ns : values()) {
            if (ns.URL.equalsIgnoreCase(URL)) {
                result = ns;
                break;
            }
        }
        return result;
    }
}
