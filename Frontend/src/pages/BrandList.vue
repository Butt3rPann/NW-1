<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItems, deleteItemById , getItemById} from '@/libs/fetchUtils'
import ListModel from '@/components/model/ListModel.vue'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import WarningMessage from '@/components/elements/WarningMessage.vue'
import ItemNotFound from '@/components/elements/ItemNotFound.vue'
import emptySaleItems from '@/assets/images/emptySaleItems.png'

const brands = ref([])
const showNotFound = ref(false)

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
	const brand = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id)
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
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, deletedId.value)
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
<div class="bg-white text-[#332A1E]">
    <div v-if="!showNotFound">
        <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25" />
        <div class="font-rubik mx-35 pb-15 pt-30">
            <div class="flex justify-between items-center mb-4">
                <p class="text-[3.5rem] font-bold text-[#332A1E]">Brands</p>
                <router-link :to="{ name: 'AddBrand' }">
                    <BaseButton :icon="addIcon" text="Add Brand" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-add-button"/>
                </router-link>
            </div>
            <p class="font-medium text-lg mb-7">
                <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
                <span class="text-[#332A1E]/50 mx-3"> > </span>
                <span class="text-[#6F879C]">Brands</span>
            </p>
            <div class="border border-[#CFC8BE] rounded-md overflow-hidden bg-white gap-2">
                <table class="w-full ">
                    <thead>
                        <tr class="flex bg-[#F9F5F5] font-semibold border-b border-[#CFC8BE] h-16 items-center">
                            <th class="w-[25%] p-2 text-center">id</th>
                            <th class="w-[75%] p-2">Name</th>
                            <th class="w-[25%] p-2">Action</th>
                        </tr>
                    </thead>
                    <tbody class="itbms-row">
                        <tr v-for="brand in brands" :key="brand.id" class="flex items-center border-t border-[#CFC8BE] h-17">
                            <td class="itbms-id w-[25%] p-2 text-center">{{ brand.id }}</td>
                            <td class="itbms-name w-[75%] p-2 text-center">{{ brand.name }}</td>
                            <td class="w-[25%] p-2">
                                <div class="flex justify-center gap-3">
                                    <router-link :to="{ name: 'EditBrand', params: { id: brand.id } }" 
                                        class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5 hover:bg-[#6F879C] hover:text-[#F2EDEC]">
                                        E
                                    </router-link>
                                    <p @click="deleteBrandById(brand.id)" 
                                        class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] cursor-pointer py-1 px-2.5 hover:bg-[#D27B7B] hover:text-[#F2EDEC]">
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
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" :message="`Do you want to delete ${brandToDelete} brand?`"
            class="itbms-message"
            @delete="deleteBrand" />
        <WarningMessage v-if="showCannotDeletePopup" @close="closeDelConfirm" :message="`Delete ${brandToDelete} is not allowed. There are sale items with ${brandToDelete} brand.`"/>
    </div>
    <ItemNotFound v-else title="Brands" description="An error has occurred, the brand does not exist." backPathName="BrandList" />
</div>
</template>

<style scoped></style>
