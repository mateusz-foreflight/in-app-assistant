import Resource from "../../types/Resource";
import {api} from "../../client";
import MenuChoice from "../../types/MenuChoice";
import Source from "../../types/Source";
import Metric from "../../types/Metric";


export namespace cache {
    let sourceCache: Record<number, Source> = {};
    let resourceCache: Record<number, Resource> = {};
    let menuChoiceCache: Record<number, MenuChoice> = {};
    // key: id of menu choice   value: list of children of that menu choice
    let menuChoiceChildrenCache: Record<number, MenuChoice[]> = {};
    // Stores all top level menu choices
    let menuChoiceTopLevelCache: MenuChoice[] = [];
    let metricCache: Record<number, Metric> = {};

    export const getSourceFromId = (id: number) : Source | null => {
        return sourceCache[id] ?? null;
    }

    export const getAllSources = () => {
        let sources: Source[] = [];
        for(const id in sourceCache){
            sources.push(sourceCache[id]);
        }
        return sources;
    }

    export const getResourceFromId = (resourceId: number) : Resource | null => {
        return resourceCache[resourceId] ?? null;
    }

    export const getResourcesFromIds = (resourceIds: number[]) : Resource[] => {
        let resources: Resource[] = [];
        for (let id of resourceIds) {
            let resource = getResourceFromId(id);
            if(resource !== null){
                resources.push(resource);
            }
        }
        return resources;
    }

    export const getAllResources = () => {
        let resources: Resource[] = [];
        for(const id in resourceCache){
            resources.push(resourceCache[id]);
        }
        return resources;
    }

    export const getMenuChoiceFromId = (id: number) : MenuChoice | null => {
        return menuChoiceCache[id] ?? null;
    }

    export const getMenuChoiceChildrenFromId = (id: number) : MenuChoice[] => {
        return menuChoiceChildrenCache[id];
    }

    export const getMenuChoicesFromIds = (ids: number[]) : MenuChoice[] => {
        let choices: MenuChoice[] = [];
        for (let id of ids) {
            let choice = getMenuChoiceFromId(id);
            if(choice !== null){
                choices.push(choice);
            }
        }
        return choices;
    }

    export const getAllMenuChoices = () => {
        let choices: MenuChoice[] = [];
        for(const id in menuChoiceCache){
            choices.push(menuChoiceCache[id]);
        }
        return choices;
    }

    export const getTopLevelMenuChoices = () : MenuChoice[] => {
        return [...menuChoiceTopLevelCache];
    }

    export const getAllMetrics = () => {
        let metrics: Metric[] = [];
        for(const id in metricCache){
            metrics.push(metricCache[id]);
        }
        return metrics;
    }

    export const refresh = async () => {
        await updateSources();
        await updateResources();
        await updateMenuChoices();
        await updateMetrics();
    }

    // Also updates the cache that stores child relationships, as well as the cache that stores top level menu choices
    export const updateMenuChoices = async () => {
        menuChoiceCache = {}
        menuChoiceChildrenCache = {};
        let retrievedChoices = await api.extract<MenuChoice>(api.getAllMenuChoices());
        for (const retrievedChoice of retrievedChoices) {
            menuChoiceCache[retrievedChoice.id] = retrievedChoice;
            menuChoiceChildrenCache[retrievedChoice.id] = await api.extract<MenuChoice>(api.getChildrenById(retrievedChoice.id))
        }

        menuChoiceTopLevelCache = await api.extract<MenuChoice>(api.getTopLevelMenuChoices());
    }

    export const updateResources = async () => {
        resourceCache = {}
        let retrieved = await api.extract<Resource>(api.getAllResources());
        retrieved.forEach(item => {
            resourceCache[item.id] = item;
        })
    }

    export const updateSources = async () => {
        sourceCache = {}
        let retrieved = await api.extract<Source>(api.getAllSources());
        retrieved.forEach(item => {
            sourceCache[item.id] = item;
        })
    }

    export const updateMetrics = async () => {
        metricCache = {}
        let retrieved = await api.extract<Metric>(api.getAllMetrics());
        retrieved.forEach(item => {
            metricCache[item.id] = item;
        })
    }
}
