<script setup>
import { useRouter } from 'vue-router'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { addSaleItem } from '@/libs/fetchUtils'
import { ref } from 'vue'

const router = useRouter()

const addNewSaleItem = async (newSaleItemData) => {
    try {
        await addSaleItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, newSaleItemData)
    } catch (error) {
        console.log(error);
    }
}

const disabled = ref(false)

const handleNewSaleItem = async (newSaleItem) => {
    await addNewSaleItem(newSaleItem);
    disabled.value = true
    router.push('/sale-items');
}
</script>

<template>
    <div>
        <SaleItemForm @submitAction="handleNewSaleItem" :disabled="disabled"/>
    </div>
</template>

<style scoped></style>