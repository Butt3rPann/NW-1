import { defineStore ,acceptHMRUpdate} from 'pinia'
import { ref } from 'vue'

export const useSaleItemGalleryStore = defineStore('saleItemGallery', () => {
    const currentPage = ref(1)
    const currentFilter = ref([])
    const currentSort = ref('none')

    const getPage = () => {
        return currentPage.value
    }

    const getSort = () => {
        return currentSort.value
    }

    const getFilter = () => {
        return currentFilter.value
    }

    const goToPage = (page) => {
        currentPage.value = page
    }

    const prevPage = (isFirstPage) => {
        if (!isFirstPage) {
            currentPage.value -= 1
        }
    }

    const nextPage = (isLastPage) => {
        if (!isLastPage) {
            currentPage.value += 1
        }
    }

    const lastPage = (totalPage) => {
        currentPage.value = totalPage
    }

    const resetPage = () => {
        currentPage.value = 1
    }

    const changeSort = (type) => {
        currentSort.value = type
        resetPage()
    }

    const clearFilter = () => {
        currentFilter.value = []
    }

    const deleteFilter = (index) => {
        currentFilter.value.splice(index, 1)
    }

    return {
        currentPage, getPage, goToPage, prevPage ,nextPage, lastPage, resetPage, 
        currentFilter, getFilter, clearFilter, deleteFilter,
        currentSort, getSort, changeSort
    }
})

if (import.meta.hot){
    import.meta.hot.accept(acceptHMRUpdate(useSaleItemGalleryStore, import.meta.hot))
}