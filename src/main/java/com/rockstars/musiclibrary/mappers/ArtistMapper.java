package com.rockstars.musiclibrary.mappers;

import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.model.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = SongMapper.class)
public interface ArtistMapper {
    
    ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    Artist toEntity(ArtistDTO artistDTO);

    ArtistDTO toDTO(Artist artist);
    
    List<ArtistDTO> toDTOList(List<Artist> artists);
    
    List<Artist> toEntityList(List<ArtistDTO> artistDTOs);
}