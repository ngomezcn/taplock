package app.taplock.sapo;

import app.taplock.sapo.add_itap.AddItapModel;
import app.taplock.sapo.emailverification.EmailVerificationModel;
import app.taplock.sapo.emailverification.ResendVerificationModel;
import app.taplock.sapo.home.GetItapsModel;
import app.taplock.sapo.sign_in.BannerModel;
import app.taplock.sapo.sign_in.SignInModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


import app.taplock.sapo.signup.SignUpModel;

public interface RetrofitAPI {

    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST("test/")
    //on below line we are creating a method to post our data.
    Call<DataModal> createPost(@Body DataModal dataModal);

    @POST("sign-up/")
    Call<SignUpModel> createPost(@Body SignUpModel dataModel);

    @POST("sign-in/")
    Call<EmailVerificationModel> createPost(@Body EmailVerificationModel dataModel);

    @POST("sign-in/")
    Call<SignInModel> createPost(@Body SignInModel dataModel);

    @POST("resend-email/")
    Call<ResendVerificationModel> createPost(@Body ResendVerificationModel dataModel);

    @POST("claim-itap/")
    Call<AddItapModel> createPost(@Body AddItapModel dataModel);

    @POST("get-my-itaps/")
    Call<GetItapsModel> createPost(@Body GetItapsModel dataModel);

    @POST("get-banner-message/")
    Call<BannerModel> createPost(@Body BannerModel dataModel);
}