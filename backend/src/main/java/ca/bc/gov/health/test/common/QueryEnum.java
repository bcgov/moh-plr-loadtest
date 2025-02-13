package ca.bc.gov.health.test.common;

public enum QueryEnum {

    PROVIDER_ID("PROVIDER_ID", "select p.CREATED_DTS, PROVIDER_CHID from PLR.PRS_PROVIDERS p JOIN GRS_IDENTIFIERS i ON p.PAUTH_ID = i.PAUTH_PAUTH_ID where i.IDENTIFIER_TYPE_CODE IN (select ctl_Id from PLR.PRS_CT_IDENTIFIER_TYPES WHERE CTL_NAME_CODE IN ('%s')) ORDER BY CREATED_DTS DESC");

    private final String identifier;
    private final String query;

    QueryEnum(String identifier, String query) {
        this.identifier = identifier;
        this.query = query;
    }

    public String identifier() {
        return identifier;
    }

    public String query() {
        return query;
    }


    public static QueryEnum findByIdentifier(String id) {
        QueryEnum result = null;
        for (QueryEnum ns : values()) {
            if (ns.identifier.equalsIgnoreCase(id)) {
                result = ns;
                break;
            }
        }
        return result;
    }

   
}
