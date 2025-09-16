package br.com.ms_produtos.repository;

import br.com.ms_produtos.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Produto findById(int id);
}
