<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItems, deleteItemById , getItemById} from '@/libs/fetchUtils'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import WarningMessage from '@/components/elements/WarningMessage.vue'
import ErrorMessage from '@/components/elements/ErrorMessage.vue'
import emptySaleItems from '@/assets/images/emptySaleItems.png'
import productNotFound from '@/assets/images/product-not-found.png'
import { useUserStore } from '@/stores/UserStore'

const brands = ref([])
const showNotFound = ref(false)
const userStore = useUserStore()

onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
    } catch (error) {
        console.log(error);
    }
})

const isShowPopup = ref(false)
const message = ref('')
const deletedId = ref(null)
const route = useRoute()
const router = useRouter()

const brandToDelete = ref(null)

if (route.query.added === 'true') {
    router.replace({query: { }})
    message.value = 'The brand has been added.'
    isShowPopup.value = true
} else if (route.query.edited === 'true') {
    router.replace({query: { }})
    message.value = 'The brand has been updated.'
    isShowPopup.value = true
}

const showCannotDeletePopup = ref(false)

async function deleteBrandById(id, name) {
    try {
        deletedId.value = id
        brandToDelete.value = name
        const brand = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id, userStore.getAccessToken())
        if (brand.noOfSaleItems > 0) {
            showCannotDeletePopup.value = true
            } else {
            showDelConfirm.value = true
        }
    } catch (error) {
        console.error(error)
  }
}

const showDelConfirm = ref(false)

function closeDelConfirm() {
    showDelConfirm.value = false
    showCannotDeletePopup.value = false
    deletedId.value = null
}

async function deleteBrand() {
    isShowPopup.value = false
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, deletedId.value, userStore.getAccessToken())
        if (status === 404) {
            return showNotFound.value = true
        } else {
            brands.value = brands.value.filter(item => item.id !== deletedId.value)
            message.value = "The brand has been deleted."
            showDelConfirm.value = false
            isShowPopup.value = true
            setTimeout(() => isShowPopup.value = false, 2500)
        }
    } catch (error) {
        console.log(error);
    }
}

</script>

<template>
<div class="bg-white text-[#332A1E] font-rubik">
    <div v-if="!showNotFound">
        <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
        <div class="mx-auto px-7 pb-15 pt-20 md:pt-30 max-w-300">
            <div class="flex justify-between items-center mb-4">
                <p class="text-3xl sm:text-4xl lg:text-5xl font-bold text-[#332A1E]">Brands</p>
                <router-link :to="{ name: 'AddBrand' }">
                    <BaseButton :icon="addIcon" text="Add Brand" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-add-button"/>
                </router-link>
            </div>
            <p class="font-medium text-sm md:text-lg lg:text-lg mb-7">
                <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E]  cursor-pointer">All Sale Items</span></router-link>
                <span class="text-[#332A1E]/50 mx-2 md:mx-3"> > </span>
                <span class="text-[#6F879C]">Brands</span>
            </p>
            <div class="border border-[#CFC8BE] rounded-md overflow-x-auto bg-white">
                <table class="table-auto w-full">
                    <thead>
                        <tr class="bg-[#F9F5F5] font-semibold border-b border-[#CFC8BE] h-15 text-center text-sm md:text-lg">
                            <th class="pl-5">id</th>
                            <th class="p-5">Name</th>
                            <th class="pr-5">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="brand in brands" :key="brand.id" class="itbms-row border-t border-[#CFC8BE] h-15 text-center text-sm md:text-lg">
                            <td class="itbms-id pl-5">{{ brand.id }}</td>
                            <td class="itbms-name p-5">{{ brand.name }}</td>
                            <td class="pr-5">
                                <div class="flex justify-center gap-1 md:gap-3">
                                    <router-link :to="{ name: 'EditBrand', params: { id: brand.id } }" 
                                        class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5 hover:bg-[#6F879C] hover:text-[#F2EDEC]">
                                        E
                                    </router-link>
                                    <p @click="deleteBrandById(brand.id, brand.name)" 
                                        class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] py-1 px-2.5 hover:bg-[#D27B7B] hover:text-[#F2EDEC]">
                                        D
                                    </p>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div v-if="!brands.length" class="flex flex-col items-center space-y-3 py-18">
                    <img :src="emptySaleItems" alt="EmptySaleItems" class="w-20">
                    <p class="text-xl text-[#ABBCC9]">no brand</p>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" :message="`Do you want to delete ${brandToDelete} brand?`" class="itbms-message" @delete="deleteBrand" />
        <WarningMessage v-if="showCannotDeletePopup" @close="closeDelConfirm" :message="`Delete ${brandToDelete} is not allowed. There are sale items with ${brandToDelete} brand.`"/>
    </div>
    <ErrorMessage v-else title="Brand" description="An error has occurred, the brand does not exist." backPathName="BrandList" :img="productNotFound">
        <span>Brand</span><br/>
        <span>Not Found</span>
    </ErrorMessage>
</div>
</template>

<style scoped></style>
