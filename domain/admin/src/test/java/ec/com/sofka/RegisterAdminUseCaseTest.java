package ec.com.sofka;

import ec.com.sofka.gateway.AdminRepository;
import ec.com.sofka.gateway.JwtService;
import ec.com.sofka.gateway.PasswordHasher;
import ec.com.sofka.usecases.RegisterAdminUseCase;
import ec.com.sofka.usecases.command.RegisterAdminCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterAdminUseCaseTest {
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RegisterAdminUseCase useCase;

    @Test
    void shouldThrowConflictExceptionWhenAdminAlreadyExists() {
        String email = "admin@test.com";
        String password = "password123";
        String hashedPassword = "hashedPassword";

        AdminDTO existingAdmin = new AdminDTO("admin-1", email, hashedPassword, RoleEnum.ADMIN);

        when(adminRepository.findByEmail(email)).thenReturn(Mono.just(existingAdmin));

        RegisterAdminCommand command = new RegisterAdminCommand(email, password, RoleEnum.ADMIN);

        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException &&
                                throwable.getMessage().equals("Admin already exists"))
                .verify();

        verify(adminRepository, times(1)).findByEmail(email);
        verifyNoInteractions(passwordHasher, jwtService);
    }

    @Test
    void shouldRegisterAdminSuccessfullyWhenEmailDoesNotExist() {
        String email = "admin@test.com";
        String role = RoleEnum.SUPER_ADMIN.name();
        String password = "password123";
        String hashedPassword = "hashedPassword";
        String token = "jwtToken";

        when(adminRepository.findByEmail(email)).thenReturn(Mono.empty());
        when(passwordHasher.hashPassword(password)).thenReturn(hashedPassword);
        when(adminRepository.save(any(AdminDTO.class))).thenReturn(Mono.just(new AdminDTO("admin-1", email, hashedPassword, RoleEnum.SUPER_ADMIN)));
        when(jwtService.generateToken(email, role)).thenReturn(token);

        RegisterAdminCommand command = new RegisterAdminCommand(email, password, RoleEnum.SUPER_ADMIN);

        StepVerifier.create(useCase.execute(command))
                .consumeNextWith(response -> {
                    assertNotNull(response);
                    assertEquals("admin-1", response.getId());
                    assertEquals(email, response.getEmail());
                    assertEquals(token, response.getToken());
                })
                .verifyComplete();

        verify(adminRepository, times(1)).findByEmail(email);
        verify(passwordHasher, times(1)).hashPassword(password);
        verify(adminRepository, times(1)).save(any(AdminDTO.class));
        verify(jwtService, times(1)).generateToken(email, role);
    }
}
