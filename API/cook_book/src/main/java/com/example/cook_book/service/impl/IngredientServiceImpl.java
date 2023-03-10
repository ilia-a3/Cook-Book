package com.example.cook_book.service.impl;

import com.example.cook_book.entity.Ingredient;
import com.example.cook_book.exception.ResourceNotFoundException;
import com.example.cook_book.payload.IngredientDto;
import com.example.cook_book.repository.IngredientRepository;
import com.example.cook_book.service.IngredientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepository ingredientRepository;
    private ModelMapper mapper;

    public ModelMapper getMapper() {
        return mapper;
    }

    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public IngredientServiceImpl(IngredientRepository ingredientRepository, ModelMapper mapper) {
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public IngredientDto addIngredient(IngredientDto ingredientDto) {

        ingredientRepository.save(mapToEntity(ingredientDto));
        return ingredientDto;
    }

    @Override
    public IngredientDto getIngredientById(long id) {
        Optional<Ingredient> foundIngredient = ingredientRepository.findById(id);

        if (foundIngredient.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return mapToDto(foundIngredient.get());
    }

    @Override
    public List<IngredientDto> getIngredients() {
        return ingredientRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String removeIngredient(long id) {
        if (!ingredientExistsById(id)) {
            throw new ResourceNotFoundException();
        }
        ingredientRepository.deleteById(id);
        return "successfully deleted ingredient with an id of " + id;
    }

    @Override
    public IngredientDto updateIngredient(IngredientDto ingredientDto, long id) {
        if (!ingredientExistsById(id)) {
            throw new ResourceNotFoundException();
        }
        Ingredient newIngredient = ingredientRepository.getReferenceById(id);

        if (ingredientDto.getName() != null) {
            newIngredient.setName(ingredientDto.getName());
        }

        ingredientRepository.save(newIngredient);
        return mapToDto(newIngredient);
    }

    private boolean ingredientExistsById(long id) {
        return ingredientRepository.existsById(id);
    }

    private IngredientDto mapToDto(Ingredient entity) {
        return mapper.map(entity, IngredientDto.class);
    }
    private Ingredient mapToEntity(IngredientDto dto) {
        return mapper.map(dto, Ingredient.class);
    }


}
