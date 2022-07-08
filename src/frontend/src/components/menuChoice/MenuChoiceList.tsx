import React from "react";
import {Heading} from "@foreflight/ffui";
import SelectableList from "../common/SelectableList";
import MenuChoice from "../../types/MenuChoice";
import {cache} from "../common/Cache";

type MenuChoiceListProps = {
    choices: MenuChoice[];
    selectCallback: (choice: MenuChoice) => void;
    searchCallback: (searchVal: string) => void;
    selectedChoiceId: number | null;
}

class MenuChoiceList extends React.Component<MenuChoiceListProps, {}>{

    render() {
        return(
            <>
                <Heading>
                    Menu Choice List:
                </Heading>

                <SelectableList<MenuChoice>
                    columnNames={["Menu Choice Name", "Parent Name", "Children", "Resources"]}
                    items={this.props.choices}
                    getIdFunc={choice => choice.id}
                    selectedItemId={this.props.selectedChoiceId}
                    selectCallback={this.props.selectCallback}
                    searchCallback={this.props.searchCallback}
                    columnFuncs={[
                        choice => choice.name,
                        choice => choice.parentId ? cache.getMenuChoiceFromId(choice.parentId)!.name : "",
                        choice => {
                            return {raw:
                                    <>{cache.getMenuChoiceChildrenFromId(choice.id).map((child) => (
                                        <div key={child.id}>{child.name}</div>
                                    ))}</>
                            };
                        },
                        choice => {
                            return {raw:
                                    <>{cache.getResourcesFromIds(choice.resourceIds).map((resource) => (
                                        <div key={resource.id}>{resource.name}</div>
                                    ))}</>
                            };
                        }
                    ]}
                />
            </>
        )
    }
}

export default MenuChoiceList;