package ewm.compilation.dto;

import ewm.compilation.model.Compilation;
import ewm.event.dto.EventShortDto;
import ewm.event.model.Event;

import java.util.Set;

public class CompilationMapper {
    public static Compilation requestToCompilation(NewCompilationDto request, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(request.getPinned());
        compilation.setTitle(request.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto compilationToDto(Compilation compilation, Set<EventShortDto> eventShortDtos) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }


}
