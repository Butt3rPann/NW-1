<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { updateFormData, getItemById } from '@/libs/fetchUtils'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import ItemNotFound from '@/components/elements/ItemNotFound.vue'

const router = useRouter()
const { params: { id } } = useRoute()
const saleItem = ref({})
const prevPath = ref(null)
const prevPathName = ref(null)
const prevParams = ref(null)

const previousPage = () => {
    prevPath.value = router.options.history.state.back
    if (prevPath.value) {
        const resolve = router.resolve(prevPath.value)
        prevPathName.value = resolve.name
        prevParams.value = resolve.params.saleItemId ?? null
    } else {
        prevPathName.value = 'SaleItemsDetail'
        prevParams.value = id
    }
}

const filePath = ref([])
const imageData = ref([])

onMounted(async () => {
    try {
        saleItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, id)
        saleItem.value.saleItemImages.forEach(img => {
            imageData.value.push({order: img.imageViewOrder, fileName: img.fileName, status: 'ONLINE', imageFile: null})
        })
        filePath.value = imageData.value.map(img => `${import.meta.env.VITE_APP_URL}/v1/files/${img.fileName}`)
        previousPage()
    } catch (error) {
        console.log(error);
    }
})

const handleEditSaleItem = async (editedItem, saleItemImg, dataChanged, imagesChanged) => {
    try { 
        await updateFormData(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, id, saleItemImg, editedItem, dataChanged, imagesChanged)
        if (prevPathName.value === 'SaleItemsList') {
            router.push({ name: prevPathName.value , query: { edited: 'true' }})
        } else {
            router.push({ name: 'SaleItemsDetail', params: { saleItemId: id}, query: { edited: 'true' } })
        }
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div v-if="saleItem.id" class="px-10 md:px-22 lg:px-25 pt-22 md:pt-30 pb-13 font-rubik bg-white">
        <p class=" font-medium text-sm md:text-base lg:text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: id } }"><span class="itbms-back-button text-[#6F879C]">{{ saleItem.model }}</span></router-link>
        </p>
        <SaleItemForm @submitAction="handleEditSaleItem" :saleItemData="saleItem" :pathName="prevPathName" :params="prevParams" :imageData="imageData" :filePath="filePath"/>
    </div>
    <ItemNotFound v-else title="Sale Item" description="The requested sale item does not exist." :backPathName="prevPathName === 'SaleItemsList' ? prevPathName : 'SaleItems'" />
</template>

<style scoped></style>