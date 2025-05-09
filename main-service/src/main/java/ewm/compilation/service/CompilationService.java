package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.PatchCompilationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface CompilationService {
    CompilationDto postCompilation(@Valid @NotNull NewCompilationDto request);

    CompilationDto patchCompilation(@Valid @NotNull PatchCompilationDto request, @NotNull @Positive Long compId);

    void deleteCompilation(@NotNull @Positive Long compId);

    CompilationDto getCompilationById(@NotNull @Positive Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
