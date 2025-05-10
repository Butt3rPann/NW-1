<script setup>
import { useRouter } from 'vue-router'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { addItem } from '@/libs/fetchUtils'
import { ref } from 'vue'

const router = useRouter()

const addNewSaleItem = async (newSaleItemData) => {
    try {
        await addItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, newSaleItemData)
    } catch (error) {
        console.log(error);
    }
}

const disabled = ref(false)

const emit = defineEmits(['addSuccess'])

const handleNewSaleItem = async (newSaleItem) => {
    try {
        console.log(newSaleItem);
        
        await addNewSaleItem(newSaleItem);
        disabled.value = true
        router.push({ name: 'SaleItems', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div>
        <SaleItemForm @submitAction="handleNewSaleItem" path="/sale-items" :disabled="disabled" />
    </div>
</template>

<style scoped></style>