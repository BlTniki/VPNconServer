package com.bitniki.VPNconServer.modules.metacodes.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.metacodes.exception.MetacodeValidationException;
import com.bitniki.VPNconServer.modules.metacodes.model.Metacode;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToCreate;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToUse;
import com.bitniki.VPNconServer.modules.metacodes.service.MetacodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/metacodes")
public class MetacodeController {
    @Autowired
    private MetacodeService metacodeService;

    /**
     * Создание метакода для применения операции юзером.
     * Предполагаются повышенные права доступа.
     * @param metacodeToCreate Тело запроса с необходимой операцией.
     * @return Модель {@link Metacode} с новым метакодом и операцией, с которой ассоциирован метакод.
     * @throws MetacodeValidationException неудача с определением необходимой операцией.
     */
    @PostMapping("/gen")
    @PreAuthorize("hasAuthority('metacode:gen')")
    ResponseEntity<Metacode> generateNewCode(@RequestBody MetacodeToCreate metacodeToCreate)
            throws MetacodeValidationException {
        return ResponseEntity.ok(
                Metacode.toModel(metacodeService.generateCode(metacodeToCreate))
        );
    }

    /**
     * Применение метакодом реквестирущим юзером (юзер должен быть авторизированным).
     * При применении выполняется ассоциированная операция.
     * @param principal Объект Principal, содержащий информацию о текущем пользователе.
     * @param metacodeToUse Тело запроса с метакодом.
     * @return Строку "Success".
     * @throws EntityNotFoundException Если метакод или юзер не найден.
     */
    @PostMapping("/use")
    @PreAuthorize("hasAuthority('metacode:use')")
    ResponseEntity<String> useCode(Principal principal, @RequestBody MetacodeToUse metacodeToUse)
            throws EntityNotFoundException {
        return ResponseEntity.ok(
                metacodeService.resolveMetacodeByLogin(principal.getName(), metacodeToUse)
        );
    }
}
