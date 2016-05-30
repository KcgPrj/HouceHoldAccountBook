package jp.ac.kcg.repository

import jp.ac.kcg.domain.ItemCategory
import jp.ac.kcg.domain.ItemCategoryPK
import org.springframework.data.jpa.repository.JpaRepository

interface ItemCategoryRepository: JpaRepository<ItemCategory, ItemCategoryPK>
