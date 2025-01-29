/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.bc.gov.health.test.common;

/**
 *
 * @author camille.estival
 */
public enum PlrNamingSystemEnum {
    
        IPC ("IPC", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-plr-ipc"), //could change if we leave it in CanadianRegistry
        CPN ("CPN", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-plr-common-party-number"),
        ORGID ("ORGID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-plr-org-id"),
        IFC ("IFC", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-plr-ifc"),
        HFI ("HFI", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-msp-facility-id"),
        CPSID ("CPSID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-physician"),
        RNID ("RNID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-nurse"),
        CCID ("CCID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-clinical-counsellor"),
        CHIROID ("CHIROID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-chiropractor"),
        COUNID ("COUNID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-psychotherapy-counsellor"),
        DENID ("DENID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-dentist"),
        KNID ("KNID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-kinesiologist"),
        MFTID ("MFTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-family-therapist"),
        NDID ("NDID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-naturopathic-physician"),
        OPTID ("OPTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-optometrist"),
        OTID ("OTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-occupational-therapist"),
        PHTID ("PHTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-pharmacy-technician"),
        PHYSIOID ("PHYSIOID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-physical-therapist"),
        POID ("POID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-podiatric-surgeon"),
        PSYCHID ("PSYCHID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-psychologist"),
        RACID ("RACID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-acupuncturist"),
        RDID ("RDID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-dietitian"),
        RMID ("RMID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-midwife"),
        RMTID ("RMTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-massage-therapist"),
        SWID ("SWID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-social-worker"),
        HAID ("HAID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-health-authority-id"), // TODO confirm if needed and if need to be created in Bc-core
        MPID ("MPID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-ministry-practitioner-id"), // TODO confirm if we need it
        OOPID ("OOPID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-out-of-province-provider"), 
        ENPID ("ENPID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-electroneurophysiology-technologist"), // TODO to confirm if need to be created at a national or BC level
        MOAID ("MOAID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-office-assistant"), // TODO to confirm if need to be created at a national or BC level
        MRTID ("MRTID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-radiation-technologist"), // TODO to confirm if need to be created at a national or BC level
        PHID ("PHID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-license-pharmacist"), // TODO check that the naming system is fixed in canadian registry
        PHYID ("PHYID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-registered-pharmacy"), // TODO need to fix the naming system in canadian registry
        PPID ("PPID", "https://fhir.infoway-inforoute.ca/NamingSystem/ca-bc-paramedic"), // TODO confirm that this need to be created in canadian or bc level
        SRID ("SRID", "https://health.gov.bc.ca/fhir/NamingSystem/ca-bc-special-register-genera"); // TODO confirm that this one is needed

        private final String identifierType;  
        private final String namingSystemURL; 
    
        PlrNamingSystemEnum(String identifierType, String namingSystemURL) {
            this.identifierType = identifierType;
            this.namingSystemURL = namingSystemURL;
        }
        public String identifierType() { 
            return identifierType;
        } 
        public String namingSystemURL() {
            return namingSystemURL;
        } 
        
    public static PlrNamingSystemEnum findByIdentifierType(String idType) {
        PlrNamingSystemEnum result = null;
        for (PlrNamingSystemEnum ns : values()) {
            if (ns.identifierType.equalsIgnoreCase(idType)) {
                result = ns;
                break;
            }
        }
        return result;
    }
    
    public static PlrNamingSystemEnum findByUrl(String namingSystemUrl) {
        PlrNamingSystemEnum result = null;
        for (PlrNamingSystemEnum ns : values()) {
            if (ns.namingSystemURL.equalsIgnoreCase(namingSystemUrl)) {
                result = ns;
                break;
            }
        }
        return result;
    }
}
