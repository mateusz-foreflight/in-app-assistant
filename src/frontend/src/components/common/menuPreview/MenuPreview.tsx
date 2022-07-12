import React from "react";
import {Button, Row} from "@foreflight/ffui";
import MenuChoice from "../../../types/MenuChoice";
import ResourceView from "./ResourceView";
import Resource from "../../../types/Resource";
import {cache} from "../Cache";

type MenuChoiceSelectable = MenuChoice &
    {selected: boolean}

type MenuPreviewProps = {
    visitMenuChoiceCallback?: (choice: MenuChoice) => void;
    visitResourceCallback?: (resource: Resource) => void;
}

type MenuPreviewState = {
    choices: MenuChoiceSelectable[][],
    displayResourceView: boolean,
    resourcesToDisplay: Resource[]
}

class MenuPreview extends React.Component<MenuPreviewProps, MenuPreviewState>{

    constructor(props: MenuPreviewProps) {
        super(props)
        this.state = {
            choices: [],
            displayResourceView: false,
            resourcesToDisplay: []
        }
    }

    async componentDidMount() {
        await cache.refresh();

        let topLevelChoices: MenuChoiceSelectable[] = cache.getTopLevelMenuChoices() as MenuChoiceSelectable[];

        this.setState((state) =>{
            let newChoices: MenuChoiceSelectable[][] = [];
            newChoices.push(topLevelChoices);
            newChoices[0].forEach((choice) => {
                choice.selected = false;
            })
            return {choices: newChoices}
        })
    }

    async buttonClick(row: number, choiceIdx: number){
        // Ensure that state is not changed if a choice is already selected
        if(this.state.choices[row][choiceIdx].selected){
            return;
        }

        if(this.props.visitMenuChoiceCallback) {
            this.props.visitMenuChoiceCallback(this.state.choices[row][choiceIdx] as MenuChoice);
        }

        // Get children of selected choice
        let choiceSelected = this.state.choices[row][choiceIdx];
        let children: MenuChoiceSelectable[] = cache.getMenuChoiceChildrenFromId(choiceSelected.id) as MenuChoiceSelectable[];
        children.forEach((child) => {
            child.selected = false;
        })

        let newChoices: MenuChoiceSelectable[][] = this.state.choices;
        let newDisplayResourceView: boolean = this.state.displayResourceView;
        let newResourcesToDisplay: Resource[];

        // The clicked choice is marked as selected, others are deselected
        newChoices[row].forEach((choice, idx) => {
            choice.selected = choiceIdx === idx;
        })

        // Clear all rows after the selected one
        newChoices.splice(row + 1, newChoices.length);

        // Add new children of selected choice to state
        newChoices.push(children);

        if (children.length === 0) {
            newDisplayResourceView = true;
            newResourcesToDisplay = (cache.getResourcesFromIds(choiceSelected.resourceIds)).slice();

        } else {
            newDisplayResourceView = false;
            newResourcesToDisplay = [];
        }

        this.setState({
            choices: newChoices,
            displayResourceView: newDisplayResourceView,
            resourcesToDisplay: newResourcesToDisplay
        })
    }


    render(){
        return(
            <div>
                {this.state.choices.map((choiceRow, rowIdx) =>(
                    <Row key={rowIdx} margin={"5px"}>
                        {choiceRow.map((choice, choiceIdx) => (
                            <Button color={choice.selected ? "green" : "blue"} key={choice.id} onClick={() => {
                                this.buttonClick(rowIdx, choiceIdx)
                            }}>
                                {choice.name}
                            </Button>
                        ))}
                    </Row>
                ))}

                {this.state.displayResourceView &&
                    <ResourceView
                        resources={this.state.resourcesToDisplay}
                        visitResourceCallback={this.props.visitResourceCallback}
                    />}
            </div>
        );
    }
}

export default MenuPreview;