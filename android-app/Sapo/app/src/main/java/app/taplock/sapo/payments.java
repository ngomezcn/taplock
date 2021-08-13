package app.taplock.sapo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class payments extends AppCompatActivity {

    WebView payment_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://checkout.stripe.com/pay/cs_test_b1clIqGvbD0xx3RZaTHe0cEXZ7v2JruNtOODsxo5s6wJuu4yvuIW0azhyK#fidkdWxOYHwnPyd1blpxYHZxWjA0T01xbnZNU2duMWYzNkNWXG5jNFNpPTA3U21kMz19MFxCX2l%2FS0RCfFdrYWRKcVw9VDZGYHRJdTNyNFV%2FVWtsYzxGN2tKfVJ2Y3NUREJ2QXY2S0JRTH9UNTV9TnxdfzNRXScpJ2hsYXYnP34nYnBsYSc%2FJ0tEJyknaHBsYSc%2FJzQ3MDQ3PGA2KD1gZDEoMWc8NyhkNjVkKGNhYDI0MjU1ZmQwMTI1Z2A3MCcpJ3ZsYSc%2FJzczMzRkMGFnKD00PT0oMWY1Yyg9NjY8KDU3Y2M8MDBhMjVkNGE2NTA1Mid4KSdnYHFkdic%2FXlgpJ2lkfGpwcVF8dWAnPydocGlxbFpscWBoJyknd2BjYHd3YHdKd2xibGsnPydtcXF1dj8qKnFkdWlqZm4rZHV1J3gl"));
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("a"));
        startActivity(browserIntent);

        //payment_view = findViewById(R.id.payment_view);
        //payment_view.loadUrl("https://checkout.stripe.com/pay/cs_test_b1ScTQyQrK45XGNg2msAudnIQOCF8tZgdP25VCujFVU0XkUynhEDW3kZrP#fidkdWxOYHwnPyd1blpxYHZxWjA0T01xbnZNU2duMWYzNkNWXG5jNFNpPTA3U21kMz19MFxCX2l%2FS0RCfFdrYWRKcVw9VDZGYHRJdTNyNFV%2FVWtsYzxGN2tKfVJ2Y3NUREJ2QXY2S0JRTH9UNTV9TnxdfzNRXScpJ2hsYXYnP34nYnBsYSc%2FJ2Q9NDE2Yzw1KDU3YTUoMWc9PShkZjRjKDFgYDEyNTU3YTEwMjExZjJjYCcpJ2hwbGEnPydkY2RjMTAzYSg2Nj00KDFnZmMoPGNgYSg8Yzc2Mjw2ZGNjMjE3MD1nZDUnKSd2bGEnPydgNz0wNjc0YCg3MDNnKDFgMzwoPWYxMyg2MDZjMjQwYTwxMzQyYTJhMTMneCknZ2BxZHYnP15YKSdpZHxqcHFRfHVgJz8naHBpcWxabHFgaCcpJ3dgY2B3d2B3SndsYmxrJz8nbXFxdXY%2FKipxZHVpamZuK2R1dSd4JSUl");
    }
}