package br.com.ms_pedidos.repository;

import br.com.ms_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens")
    List<Pedido> findAllWithItens();
}
