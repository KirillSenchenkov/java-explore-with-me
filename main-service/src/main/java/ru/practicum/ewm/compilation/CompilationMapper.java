package ru.practicum.ewm.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;

@UtilityClass
public class CompilationMapper {
    public Compilation dtoToCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned()
        );
    }

    public CompilationDto compilationToDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
