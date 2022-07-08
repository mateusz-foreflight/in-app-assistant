import React from "react";
import MenuPreview from "../components/common/menuPreview/MenuPreview";
import Navbar from "../components/common/Navbar";
import {Button, Row, TextInput, Textarea} from "@foreflight/ffui";
import MenuChoice from "../types/MenuChoice";
import MetricDTO from "../types/MetricDTO";
import {api} from "../client";
import Resource from "../types/Resource";

type UserPreviewState = {
    isMenuOpen: boolean
    isCloseDialogOpen: boolean
    isFeedbackDialogOpen: boolean
    userName: string
    feedbackVal: string
    visitedChoiceIds: Set<number>,
    visitedResourceIds: Set<number>
}

class UserPreviewPage extends React.Component<{}, UserPreviewState> {
    constructor(props: {}) {
        super(props);

        this.state = {
            isMenuOpen: false,
            isCloseDialogOpen: false,
            isFeedbackDialogOpen: false,
            userName: "",
            feedbackVal: "",
            visitedChoiceIds: new Set<number>(),
            visitedResourceIds: new Set<number>()
        }
    }

    addVisitedMenuChoice = (choice: MenuChoice) => {
        this.setState(({visitedChoiceIds}) => ({
          visitedChoiceIds: new Set(visitedChoiceIds).add(choice.id)
        }))
    }

    addVisitedResource = (resource: Resource) => {
        this.setState(({visitedResourceIds}) => ({
            visitedResourceIds: new Set(visitedResourceIds).add(resource.id)
        }))
    }

    menuClose() {
        this.setState({
            isCloseDialogOpen: true
        })
    }

    async generateMetric(answerFound: boolean, feedbackPresent: boolean) {
        let timestamp = new Date().toISOString();

        let menuchoiceIds: number[] = [];
        for(const id of this.state.visitedChoiceIds){
            menuchoiceIds.push(id);
        }

        let resourceIds: number[] = [];
        for(const id of this.state.visitedResourceIds){
            resourceIds.push(id)
        }

        let feedback = feedbackPresent ? this.state.feedbackVal : "";

        let metric: MetricDTO = {
            answerFound: answerFound,
            timestamp: timestamp,
            ticketLink: "",
            userName: this.state.userName,
            userFeedback: feedback,
            menuchoiceIds: menuchoiceIds,
            resourceIds: resourceIds
        }

        await api.addMetric(metric);
    }


    render() {
        return (
            <>


                <Navbar activePage={4}/>

                <TextInput value={""}
                           disabled={this.state.isMenuOpen}
                           onChange={newValue => this.setState({userName: newValue})} placeholder={"User Name"}
                />

                <Button disabled={this.state.userName === ""}
                        color={this.state.isMenuOpen ? "red" : "blue"}
                        onClick={() => {
                            if(this.state.isMenuOpen){
                                this.menuClose();
                            }
                            this.setState({isMenuOpen: !this.state.isMenuOpen});
                        }}
                >
                    {this.state.isMenuOpen ? "Close Menu" : "Open Help Menu"}
                </Button>

                {this.state.isMenuOpen &&
                    <MenuPreview
                        visitMenuChoiceCallback={this.addVisitedMenuChoice}
                        visitResourceCallback={this.addVisitedResource}
                    />}

                {(this.state.isCloseDialogOpen || this.state.isFeedbackDialogOpen) &&
                    <div style={{
                        position: "fixed",
                        top: "0",
                        bottom: "0",
                        left: "0",
                        right: "0",
                        background:"rgba(0,0,0,0.5)"}}
                    />}



                <dialog open={this.state.isCloseDialogOpen} style={{top: "15%", left:"-60%", position: "absolute"}}>
                    <Row flexDirection={"column"}>
                        Did you find what you needed?

                        <Row>
                            <Button
                                color={"green"}
                                onClick={() => {
                                    this.generateMetric(true, false);
                                    this.setState({isCloseDialogOpen: false})
                                }}
                            >
                                Yes
                            </Button>

                            <Button
                                color={"red"}
                                onClick={() => {
                                    this.setState({
                                        isCloseDialogOpen: false,
                                        isFeedbackDialogOpen: true})
                                }}
                            >
                                No
                            </Button>
                        </Row>
                    </Row>
                </dialog>

                <dialog open={this.state.isFeedbackDialogOpen} style={{top: "15%", left:"-60%", position: "absolute"}}>
                    <Row flexDirection={"column"}>
                        Please describe your issue:

                        <Row width={"100%"}>

                            <Textarea
                                lookLikeInput={false}
                                minHeight={"100px"}
                                value={this.state.feedbackVal}
                                onChange={newValue => this.setState({feedbackVal: newValue})}
                            />
                        </Row>

                        <Row width={"100%"} flexJustify={"flex-end"}>
                            <Button
                                color={"gray"}
                                onClick={() => {
                                    this.generateMetric(false, false);
                                    this.setState({
                                        isFeedbackDialogOpen: false,
                                        feedbackVal: ""});
                                }}
                            >
                                Cancel
                            </Button>

                            <Button
                                color={"green"}
                                onClick={() => {
                                    this.generateMetric(false, true)
                                    this.setState({
                                        isFeedbackDialogOpen: false,
                                        feedbackVal: ""});
                                }}
                            >
                                Send
                            </Button>
                        </Row>
                    </Row>
                </dialog>
            </>
        );
    }
}

export default UserPreviewPage;