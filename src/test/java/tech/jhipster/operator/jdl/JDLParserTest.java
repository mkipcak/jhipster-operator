package tech.jhipster.operator.jdl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JDLParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parse() {
        JHipsterApplicationDefinition app = new JDLParser().parse("myApp", "1.0",
                "application {\n" +
                        "  config {\n" +
                        "    baseName gateway,\n" +
                        "    applicationType gateway,\n" +
                        "    packageName com.salaboy.gateway,\n" +
                        "    serviceDiscoveryType eureka,\n" +
                        "    authenticationType jwt,\n" +
                        "    prodDatabaseType mysql,\n" +
                        "    cacheProvider hazelcast,\n" +
                        "    buildTool maven,\n" +
                        "    clientFramework react,\n" +
                        "    useSass true,\n" +
                        "    testFrameworks [protractor]\n" +
                        "  }\n" +
                        "  entities *\n" +
                        "}\n" +
                        "application {\n" +
                        "  config {\n" +
                        "    baseName invoice,\n" +
                        "    applicationType microservice,\n" +
                        "    packageName com.salaboy.invoice,\n" +
                        "    serviceDiscoveryType eureka,\n" +
                        "    authenticationType jwt,\n" +
                        "    prodDatabaseType mysql,\n" +
                        "    buildTool maven,\n" +
                        "    serverPort 8081,\n" +
                        "    skipUserManagement true\n" +
                        "  }\n" +
                        "  entities Invoice, Shipment\n" +
                        "}\n");
        assertNotNull(app);
        assertEquals(2, app.getModules().size());
        for (JHipsterModuleDefinition md : app.getModules()) {
            if (md.getType().equals("gateway")) {
                assertModuleDefinition(md, "gateway", "gateway", null);
            } else if (md.getType().equals("microservice")) {
                assertModuleDefinition(md, "invoice", "microservice", "8081");
            }
        }

        System.out.println("Application: " + app);
    }

    @Test
    public void parseAll() {
        JHipsterApplicationDefinition app = new JDLParser().parse("myComplexApp", "1.0",
                "application {\n" +
                        "  config {\n" +
                        "    baseName gateway,\n" +
                        "    applicationType gateway,\n" +
                        "    packageName com.salaboy.gateway,\n" +
                        "    serviceDiscoveryType eureka,\n" +
                        "    authenticationType jwt,\n" +
                        "    prodDatabaseType mysql,\n" +
                        "    cacheProvider hazelcast,\n" +
                        "    buildTool maven,\n" +
                        "    clientFramework react,\n" +
                        "    useSass true,\n" +
                        "    testFrameworks [protractor]\n" +
                        "  }\n" +
                        "  entities *\n" +
                        "}\n" +
                        "application {\n" +
                        "  config {\n" +
                        "    baseName invoice,\n" +
                        "    applicationType microservice,\n" +
                        "    packageName com.salaboy.invoice,\n" +
                        "    serviceDiscoveryType eureka,\n" +
                        "    authenticationType jwt,\n" +
                        "    prodDatabaseType mysql,\n" +
                        "    buildTool maven,\n" +
                        "    serverPort 8081,\n" +
                        "    skipUserManagement true\n" +
                        "  }\n" +
                        "  entities Invoice, Shipment\n" +
                        "}\n" +
                        "\n" +
                        "application {\n" +
                        "  config {\n" +
                        "    baseName review,\n" +
                        "    applicationType microservice,\n" +
                        "    packageName com.salaboy.review,\n" +
                        "    serviceDiscoveryType eureka,\n" +
                        "    authenticationType jwt,\n" +
                        "    prodDatabaseType mysql,\n" +
                        "    buildTool maven,\n" +
                        "    serverPort 8081,\n" +
                        "    skipUserManagement true\n" +
                        "  }\n" +
                        "  entities *\n" +
                        "}\n" +
                        "\n" +
                        "/* Entities for Invoice microservice */\n" +
                        "entity Invoice {\n" +
                        "    code String required\n" +
                        "    date Instant required\n" +
                        "    details String\n" +
                        "    status InvoiceStatus required\n" +
                        "    paymentMethod PaymentMethod required\n" +
                        "    paymentDate Instant required\n" +
                        "    paymentAmount BigDecimal required\n" +
                        "}\n" +
                        "enum InvoiceStatus {\n" +
                        "    PAID, ISSUED, CANCELLED\n" +
                        "}\n" +
                        "entity Shipment {\n" +
                        "    trackingCode String\n" +
                        "    date Instant required\n" +
                        "    details String\n" +
                        "}\n" +
                        "enum PaymentMethod {\n" +
                        "    CREDIT_CARD, CASH_ON_DELIVERY, PAYPAL\n" +
                        "}\n" +
                        "relationship OneToMany {\n" +
                        "    Invoice{shipment} to Shipment{invoice(code) required}\n" +
                        "}\n" +
                        "\n" +
                        "dto * with mapstruct\n" +
                        "service Invoice, Shipment with serviceClass\n" +
                        "paginate Invoice, Shipment with pagination\n" +
                        "microservice Invoice, Shipment with invoice");
        assertNotNull(app);
        assertEquals(3, app.getModules().size());

        for (JHipsterModuleDefinition md : app.getModules()) {
            switch (md.getType()) {
                case "gateway":
                    assertModuleDefinition(md, "gateway", "gateway", null);
                    break;
                case "microservice":
                    if (md.getName().equals("invoice")) {
                        assertModuleDefinition(md, "invoice", "microservice", "8081");
                    } else {
                        assertModuleDefinition(md, "review", "microservice", "8081");
                    }
                    break;
            }
        }


        System.out.println("Application: " + app);
    }

    private void assertModuleDefinition(JHipsterModuleDefinition md, String name, String type, String port) {
        assertEquals(md.getName(), name);
        assertEquals(md.getType(), type);
        assertEquals(md.getPort(), port);
    }
}