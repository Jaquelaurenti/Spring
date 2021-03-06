package br.com.fiap.cervejaria.service.impl;

import br.com.fiap.cervejaria.entity.Cerveja;
import br.com.fiap.cervejaria.repository.CervejaRepository;
import br.com.fiap.cervejaria.service.CervejaService;
import br.com.fiap.cervejaria.dto.CervejaDTO;
import br.com.fiap.cervejaria.dto.CreateCervejaDTO;
import br.com.fiap.cervejaria.dto.PrecoCervejaDTO;
import br.com.fiap.cervejaria.dto.Tipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CervejaServiceImpl implements CervejaService {

    private CervejaRepository cervejaRepository;

    public CervejaServiceImpl(CervejaRepository cervejaRepository) {
        this.cervejaRepository = cervejaRepository;
    }

    @Override
    public List<CervejaDTO> findAll(Tipo tipo) {

        List<Cerveja> cervejaList;
        if (tipo == null) {
            cervejaList = cervejaRepository.findAll();
        } else {
            cervejaList = cervejaRepository.findAllByTipo(tipo);
        }

        return cervejaList.stream()
                .map(CervejaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CervejaDTO> findAll(Integer size, Integer page, Tipo tipo) {
        Pageable pageable = PageRequest.of(page, size);
        if (tipo == null) {
            return cervejaRepository.findAll(pageable)
                    .map(CervejaDTO::new);
        }

        return cervejaRepository.findAllByTipo(pageable, tipo)
                .map(CervejaDTO::new);
    }

    @Override
    public CervejaDTO findById(Integer id) {
        return saveAndGetCervejaDTO(getCerveja(id));
    }

    private Cerveja getCerveja(Integer id) {
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public CervejaDTO create(CreateCervejaDTO createCervejaDTO) {
        Cerveja cerveja = new Cerveja(createCervejaDTO);
        return saveAndGetCervejaDTO(cerveja);
    }

    @Override
    public CervejaDTO update(Integer id, CreateCervejaDTO createCervejaDTO) {
        Cerveja cerveja = getCerveja(id);
        cerveja.setMarca((createCervejaDTO.getMarca()));
        cerveja.setPreco(createCervejaDTO.getPreco());
        cerveja.setDataLancamento(createCervejaDTO.getDataLancamento());
        cerveja.setTeorAlcoolico(createCervejaDTO.getTeorAlcoolico());
        cerveja.setTipo(createCervejaDTO.getTipo());
        return saveAndGetCervejaDTO(cerveja);
    }

    @Override
    public CervejaDTO update(Integer id, PrecoCervejaDTO precoCervejaDTO) {
        Cerveja cerveja = getCerveja(id);
        cerveja.setPreco(precoCervejaDTO.getPreco());
        return saveAndGetCervejaDTO(cerveja);
    }

    private CervejaDTO saveAndGetCervejaDTO(Cerveja cerveja) {
        Cerveja savedCerveja = cervejaRepository.save(cerveja);
        return new CervejaDTO(savedCerveja);
    }

    @Override
    public void delete(Integer id) {
        Cerveja cerveja = getCerveja(id);
        cervejaRepository.delete(cerveja);
    }
}
