package br.com.tech.challenge.domain.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MercadoPagoAPI {

    ACCESS_TOKEN("Bearer APP_USR-6155129762548657-102711-6c6e3574529dbc4e6d5944056bb132db-700064145"),

    MERCADO_PAGO_URL("https://api.mercadopago.com"),

    CAIXA_PAGAMENTO_ID("CAIXA001"),

    USER_ID("700064145");

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
