package com.affordmed.scheduler.client;

import com.affordmed.scheduler.dto.AuthResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TokenProviderTest {

    private AuthClient authClient;

    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp() {
        authClient = mock(AuthClient.class);
        tokenProvider = new TokenProvider(authClient);
    }

    @Test
    public void testGetToken_FirstCall_FetchesToken() {
        AuthResponse response = new AuthResponse("token1", 3600);
        when(authClient.authenticate()).thenReturn(response);

        String token = tokenProvider.getToken();

        assertEquals("token1", token);
        verify(authClient, times(1)).authenticate();
    }

    @Test
    public void testGetToken_SecondCall_UsesCache() {
        AuthResponse response = new AuthResponse("token1", 3600);
        when(authClient.authenticate()).thenReturn(response);

        String token1 = tokenProvider.getToken();
        String token2 = tokenProvider.getToken();

        assertEquals("token1", token1);
        assertEquals("token1", token2);
        verify(authClient, times(1)).authenticate();
    }

    @Test
    public void testGetToken_ExpiredToken_FetchesNew() throws InterruptedException {

        AuthResponse response1 = new AuthResponse("token1", 1);
        AuthResponse response2 = new AuthResponse("token2", 3600);
        
        when(authClient.authenticate()).thenReturn(response1).thenReturn(response2);

        String token1 = tokenProvider.getToken();
        assertEquals("token1", token1);

        Thread.sleep(1001);

        String token2 = tokenProvider.getToken();

        assertEquals("token2", token2);
        verify(authClient, times(2)).authenticate();
    }
}
