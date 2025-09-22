<script setup>
import { useRouter } from 'vue-router'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { uploadFormData } from '@/libs/fetchUtils'
import { ref } from 'vue'
import { useUserStore } from '@/stores/UserStore'

const router = useRouter()
const userStore = useUserStore()
const { getAccessToken } = userStore

const prevPath = ref(null)
const prevPathName = ref(null)
prevPath.value = router.options.history.state.back
if (prevPath.value) {
    const resolve = router.resolve(prevPath.value)
    prevPathName.value = resolve.name
} else {
    prevPathName.value = 'SaleItems'
}

const handleNewSaleItem = async (newSaleItem, saleItemImg) => {
    try {
        const formData = new FormData()
        saleItemImg.map(img => img.imageFile).forEach(f => formData.append('images', f))
        if (newSaleItem.model !== null) formData.append('model', newSaleItem.model)
        if (newSaleItem.brand.id !== null && newSaleItem.brand.name !== null)  {
            formData.append('brand.id', newSaleItem.brand.id)
            formData.append('brand.name', newSaleItem.brand.name)
        }
        if (newSaleItem.description !== null) formData.append('description', newSaleItem.description)
        if (newSaleItem.price !== null) formData.append('price', newSaleItem.price)
        if (newSaleItem.ramGb !== null) formData.append('ramGb', newSaleItem.ramGb)
        if (newSaleItem.screenSizeInch !== null) formData.append('screenSizeInch', newSaleItem.screenSizeInch)
        if (newSaleItem.storageGb !== null) formData.append('storageGb', newSaleItem.storageGb)
        if (newSaleItem.color !== null) formData.append('color', newSaleItem.color)
        if (newSaleItem.quantity !== null) formData.append('quantity', newSaleItem.quantity)

        await uploadFormData(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, formData, getAccessToken())
        router.push({ name: prevPathName.value, query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div class="px-10 md:px-22 lg:px-25 pt-22 md:pt-30 pb-13 font-rubik bg-white">
        <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-2 md:mx-3"> > </span>
            <span class="text-[#6F879C]">New Sale Item</span>
        </p>
        <SaleItemForm @submitAction="handleNewSaleItem" :pathName="prevPathName" :filePath="[]" :imageData="[]"/>
    </div>
</template>

<style scoped></style>
