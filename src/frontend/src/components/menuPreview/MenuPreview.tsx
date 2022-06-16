import React from "react";
import {Button, Row} from "@foreflight/ffui";
import {getChildrenById, getTopLevelMenuChoices} from "../../client";
import MenuChoice from "../../types/MenuChoice";
import ResourceView from "./ResourceView";
import Resource from "../../types/Resource";

type MenuChoiceSelectable = MenuChoice &
    {selected: boolean}

type MenuPreviewState = {
    choices: MenuChoiceSelectable[][],
    displayResourceView: boolean,
    resourcesToDisplay: Resource[]
}

class MenuPreview extends React.Component<{}, MenuPreviewState>{

    constructor(props: {}) {
        super(props)
        this.state = {
            choices: [],
            displayResourceView: false,
            resourcesToDisplay: []
        }
    }

    async componentDidMount() {

        let topLevelChoices: MenuChoiceSelectable[] = [];
        await getTopLevelMenuChoices().then(response => {
            return response.json() as Promise<any[]>;
        }).then(data =>{
            data.forEach((choice) =>{
                topLevelChoices.push(choice as MenuChoiceSelectable)
            })
        });

        this.setState((state) =>{
            let newChoices: MenuChoiceSelectable[][] = [];
            newChoices.push(topLevelChoices);
            newChoices[0].forEach((choice) => {
                choice.selected = false;
            })
            return {choices: newChoices}
        })

        console.log(this.state);
    }

    async buttonClick(row: number, choiceIdx: number){
        // Ensure that state is not changed if a choice is already selected
        if(this.state.choices[row][choiceIdx].selected){
            return;
        }

        // Get children of selected choice
        let choiceSelected = this.state.choices[row][choiceIdx];
        let children: MenuChoiceSelectable[] = [];
        await getChildrenById(choiceSelected.id).then(response =>{
            return response.json() as Promise<any[]>
        }).then(data => {
            data.forEach((child) => {
                children.push(child as MenuChoiceSelectable);
            })
        })
        children.forEach((child) => {
            child.selected = false;
        })



        this.setState((state) =>{
            let newChoices: MenuChoiceSelectable[][] = state.choices;
            let newDisplayResourceView: boolean = state.displayResourceView;
            let newResourcesToDisplay: Resource[];

            // The clicked choice is marked as selected, others are deselected
            newChoices[row].forEach((choice, idx) => {
                choice.selected = choiceIdx === idx;
            })

            // Clear all rows after the selected one
            for(let r = row + 1; r < newChoices.length; r++){
                newChoices[r] = []
            }

            // Add new children of selected choice to state
            newChoices.push(children);

            if(children.length === 0){
                newDisplayResourceView = true;
                newResourcesToDisplay = choiceSelected.resources.slice();
                console.log(newResourcesToDisplay)
            }
            else{
                newDisplayResourceView = false;
                newResourcesToDisplay = [];
            }

            return {
                choices: newChoices,
                displayResourceView: newDisplayResourceView,
                resourcesToDisplay: newResourcesToDisplay
            };
        })
    }


    render(){
        return(
            <div>
                {this.state.choices.map((choiceRow, rowIdx) =>(
                    <Row key={rowIdx}>
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
                    <ResourceView resources={this.state.resourcesToDisplay}></ResourceView>}
            </div>
        );
    }
}

export default MenuPreview;