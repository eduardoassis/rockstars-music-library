package com.rockstars.musiclibrary.mappers;

import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.model.Song;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapper {
    
    SongMapper INSTANCE = Mappers.getMapper(SongMapper.class);
    
    Song toEntity(SongDTO songDTO);
    
    List<SongDTO> toDTOList(List<Song> songs);
    
    List<Song> toEntityList(List<SongDTO> songDTOs);

    SongDTO toDTO(Song song);
}