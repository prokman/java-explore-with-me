package ewm.compilation.controller;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.PatchCompilationDto;
import ewm.compilation.service.CompilationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@RequestBody @Valid @NotNull NewCompilationDto request) {
        return compilationService.postCompilation(request);
    }

    @PatchMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto patchCompilation(@RequestBody @Valid @NotNull PatchCompilationDto request,
                                           @PathVariable @NotNull @Positive Long compId) {
        CompilationDto compilationDto = compilationService.patchCompilation(request, compId);
        return compilationDto;
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @NotNull @Positive Long compId) {
        compilationService.deleteCompilation(compId);
    }

}
