package in.res.uril;

import in.res.dto.request.AuthRequest;
import in.res.dto.response.AuthResponse;

public final class TestConstants {
    public static AuthResponse SUCCESS_AUTH_RESPONSE = new AuthResponse(4, "QpwL5tke4Pnpja7X4");
    public static AuthRequest SUCCESS_AUTH_REQUEST = new AuthRequest("eve.holt@reqres.in", "pistol");
    public static AuthRequest ERROR_AUTH_REQUEST = new AuthRequest("sydney@fife", "");
}
