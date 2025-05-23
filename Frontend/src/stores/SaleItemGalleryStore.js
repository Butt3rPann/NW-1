import { defineStore ,acceptHMRUpdate} from 'pinia'
import { ref } from 'vue'

export const useSaleItemGalleryStore = defineStore('saleItemGallery', () => {
    const currentPage = ref(0)
    const currentFilter = ref([])

    const getPage = () => {
        return currentPage.value
    }

    const getFilter = () => {
        return currentFilter.value
    }

    const setPage = (page) => {
        currentPage.value = page
    }

    const clearFilter = () => {
        currentFilter.value = []
        currentPage.value = 0
    }

    const deleteFilter = (index) => {
        currentFilter.value.splice(index, 1)
        currentPage.value = 0
    }

    return {currentPage, getPage, setPage, currentFilter, getFilter, clearFilter, deleteFilter}
})

if (import.meta.hot){
    import.meta.hot.accept(acceptHMRUpdate(useSaleItemGalleryStore, import.meta.hot))
}