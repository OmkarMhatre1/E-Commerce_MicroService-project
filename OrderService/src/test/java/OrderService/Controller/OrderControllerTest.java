package OrderService.Controller;

import OrderService.Repository.OrderRepository;
import OrderService.Service.OrderService;
import OrderService.config.OrderServiceConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
@RequiredArgsConstructor
public class OrderControllerTest {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final MockMvc mockMvc;

    @RegisterExtension
    static WireMockExtension wireMockServer =
            WireMockExtension.newInstance()
                    .options(WireMockConfiguration
                            .wireMockConfig()
                            .port(8080))
                    .build();
    private ObjectMapper objectMapper
            = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    @BeforeEach
    void setup(){
      // getProductDetailResponse();
       doPayment();
       getPaymentDetails();
       reduceQuantity();
    }

    private void reduceQuantity() {
    }


    private void getPaymentDetails() {
        
    }

    private void doPayment() {
        
    }

//    private void getProductDetailResponse() {
//        //GET /product /1
//        wireMockServer.stubFor(WireMock.get("/product/1")
//                .willReturn(WireMock.aResponse()
//                        .withStatus(HttpStatus.OK.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                        .withBody()));
//
//
//    }

    @DisplayName("Place order and payment success test")
    @Test
    public void test_WhenPlaceOrder_DoPayment_Success(){
        //first place order
        //get order from database by orderId and check
        //Check output
    }
}