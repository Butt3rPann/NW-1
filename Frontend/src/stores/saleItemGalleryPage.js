import { defineStore ,acceptHMRUpdate} from 'pinia'
import { ref } from 'vue'

export const useSaleItemGalleryPage = defineStore('saleItemGalleryPage', () => {
  const currentPage = ref(0)

  const getCurrentPage = () => {
    return currentPage.value
  }

  const setCurrentPage = (page) => {
    currentPage.value = page
  }

  return {currentPage, getCurrentPage, setCurrentPage}
})

if (import.meta.hot){
  import.meta.hot.accept(acceptHMRUpdate(useSaleItemGalleryPage,import.meta.hot))
}