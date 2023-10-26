package br.com.tech.challenge.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MercadoPagoAPI {

    ACCESS_TOKEN("TEST-6621802098599609-102118-84e7d61f11ef646bbaf89a78e1bb2631-700064145"),
    MERCADO_PAGO_URL("https://api.mercadopago.com"),
    USER_ID("700064145"),
    CAIXA_PAGAMENTO_ID("87956324"),
    SPONSOR_ID("6621802098599609");

    final String text;

    public String text() {
        return text;
    }

    public static String getQRCodeUrl() {
        return String.format("/instore/orders/qr/seller/collectors/%s/pos/%s/qrs",
                MercadoPagoAPI.USER_ID.text(), MercadoPagoAPI.CAIXA_PAGAMENTO_ID.text()
        );
    }

}
