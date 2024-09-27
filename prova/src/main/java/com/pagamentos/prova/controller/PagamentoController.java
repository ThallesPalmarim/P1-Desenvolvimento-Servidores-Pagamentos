package com.pagamentos.prova.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pagamentos.prova.model.Pagamento;
import com.pagamentos.prova.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    
    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("{id}")
    public ResponseEntity<Pagamento> get(@PathVariable("id") Long id) {
        Pagamento pagamento = pagamentoService.find(id);
        if(pagamento != null){
            return ResponseEntity.ok(pagamento);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Pagamento> create(@RequestBody Pagamento pagamento){
        pagamentoService.create(pagamento);
        URI location = ServletUriComponentsBuilder
                                    .fromCurrentRequest()
                                    .path("/{id}").buildAndExpand(pagamento.getId())
                                    .toUri();
        return ResponseEntity.created(location).body(pagamento);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pagamento> update(@PathVariable("id") Long id, @RequestBody Pagamento status) {        
        Pagamento pagamentoAtualizado = pagamentoService.updateStatus(id, status.getStatus());
        if(pagamentoAtualizado != null) {
            return ResponseEntity.ok(pagamentoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pagamento>> findByUserId(@PathVariable("idUsuario") Long idUsuario) {
        List<Pagamento> pagamentos = pagamentoService.findByUserId(idUsuario);
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> getPagamentos(
            @RequestParam(required = false) Double valorMinimo,
            @RequestParam(required = false) Double valorMaximo) {
        
        List<Pagamento> pagamentos = pagamentoService.findByValorRange(valorMinimo, valorMaximo);
        return ResponseEntity.ok(pagamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pagamento> delete(@PathVariable("id") Long id){
        if(pagamentoService.delete(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
