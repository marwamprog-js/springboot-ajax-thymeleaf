package com.mballem.demoajax.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.demoajax.domain.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
	
	@Query(value = "SELECT p FROM Promocao p WHERE p.precoPromocao = :preco")
	Page<Promocao> findByPreco(@Param("preco") BigDecimal preco, Pageable pageable);
	
	@Query(value = "SELECT p FROM Promocao p WHERE "
			+ "p.titulo like %:search% OR "
			+ "p.site like %:search% OR "
			+ "p.categoria like %:search%")
	Page<Promocao> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageable);
	
	@Query(value = "SELECT p FROM Promocao p WHERE p.site like :site")
	Page<Promocao> findBySite(@Param("site") String site, Pageable pageable);
	
	@Query("SELECT distinct p.site FROM Promocao p WHERE p.site like %:site%")
	List<String> findSitesByTermo(@Param("site") String site);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
	void updateSomarLikes(@Param("id") Long id);
	
	@Query("SELECT p.likes FROM Promocao p WHERE p.id = :id")
	int findLikesById(@Param("id") Long id);

	@Query(value = "SELECT MAX(p.dtCadastro) FROM Promocao p")
	LocalDateTime findPromocaoComUltimaData();

	@Query(value = "SELECT COUNT(p.id) as count, MAX(p.dtCadastro) as lastDate "
			+ "FROM Promocao p "
			+ "WHERE p.dtCadastro > :ultimaData")
	Map<String, Object> countAndMaxNovasPromocoesByDtCadastro(LocalDateTime ultimaData);
	
}
