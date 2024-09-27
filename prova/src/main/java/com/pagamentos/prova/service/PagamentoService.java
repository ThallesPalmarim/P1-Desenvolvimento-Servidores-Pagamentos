package com.pagamentos.prova.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pagamentos.prova.model.Pagamento;
@Service
public class PagamentoService {
    private static List<Pagamento> pagamentos = new ArrayList<Pagamento>();
    
    public PagamentoService(){
        pagamentoFake();
    }

    private void pagamentoFake(){
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setIdUsuario(1L);
        pagamento.setValor(20.00);
        pagamento.setFormaPagamento("Boleto");
        pagamento.setStatus("Aprovado");
        pagamento.setDataPagamento(LocalDate.of(2024,10,10));
        pagamentos.add(pagamento);
    }

    public Pagamento find(Pagamento pagamento){
        return pagamentos.stream().filter(p -> p.equals(pagamento)).findFirst().orElse(null);
    }
    public Pagamento find(Long id){
        return find(new Pagamento(id));
    }

    public List<Pagamento> findByUserId(@PathVariable("idUsuario") Long idUsuario) {
        return pagamentos.stream()
                .filter(p -> p.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    public List<Pagamento> findByValorRange(Double valorMinimo, Double valorMaximo) {
        return pagamentos.stream()
                .filter(p -> (valorMinimo == null || p.getValor() >= valorMinimo) &&
                             (valorMaximo == null || p.getValor() <= valorMaximo))
                .collect(Collectors.toList());
    }

    public void create (Pagamento pagamento){
        Long newId = (long) (pagamentos.size() +1);
        pagamento.setId(newId);
        pagamento.setDataPagamento(LocalDate.now());
        pagamentos.add(pagamento);
    }

    public Pagamento updateStatus(Long id, String novoStatus) {
        Pagamento _pagamento = find(id);
        if (_pagamento != null) {
            _pagamento.setStatus(novoStatus);
            return _pagamento;
        }
        return null;
    }

    public Boolean delete(Long id){
        Pagamento _pagamento = find(id);
        if(_pagamento != null){
            pagamentos.remove(_pagamento);
            return true;
        }
        return false;
    }
}
