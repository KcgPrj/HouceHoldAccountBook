package jp.ac.kcg.repository

import jp.ac.kcg.domain.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository: JpaRepository<Item, Long>
