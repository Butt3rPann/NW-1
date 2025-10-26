<script setup>
import { editItem, getItemById} from '@/libs/fetchUtils'
import BrandForm from '@/components/form/BrandForm.vue'
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import ErrorMessage from '@/components/elements/ErrorMessage.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import productNotFound from '@/assets/images/product-not-found.png'
import { useUserStore } from '@/stores/UserStore'

const router = useRouter()
const { params: { id } } = useRoute()
const brand = ref({})
const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)
const userStore = useUserStore()

onMounted(async () => {
    try {
        brand.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id, userStore.getAccessToken())
    } catch (error) {
        console.log(error);
    }
})

const handleEditBrand = async (editedBrand) => {
    const editedItem = {...editedBrand}
    Object.keys(editedItem).forEach(key => {
        if (editedItem[key] === '') {
            editedItem[key] = null
        }
    })
    try {
        const edited = await editItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, id, editedItem, userStore.getAccessToken())
        if (edited.status === 400 || edited.status === 500) {
            throw new Error(edited.message)
        }
        router.push({ name: 'BrandList', query: { edited: 'true' } })
    } catch (error) {
        console.log(error)
        isSuccess.value = false
        message.value = 'The brand could not be updated.'
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 1500)
    }
}
</script>
 
<template>
    <div v-if="brand.id" class="flex flex-col items-center justify-center gap-7 px-10 md:px-22 lg:px-25 pt-22 md:pt-30 pb-13 lg:pb-17 bg-white">
        <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
        <p class="text-2xl md:text-4xl lg:text-5xl xl:text-6xl font-bold font-rubik text-[#332A1E] text-center leading-tight">Edit Brand</p> 
        <BrandForm @submitAction="handleEditBrand" pathName="BrandList" :brandData="brand" class="max-w-150"/>   
    </div>
    <ErrorMessage v-else title="Brand" description="The brand does not exist." backPathName="BrandList" :img="productNotFound">
        <span>Brand</span><br/>
        <span>Not Found</span>
    </ErrorMessage>
</template>
 
<style scoped></style>