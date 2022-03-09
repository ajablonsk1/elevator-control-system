import React, {useEffect, useState} from 'react';
import {Form, Button} from 'react-bootstrap'
import Api from './Api'
import { useNavigate } from "react-router-dom"
import BackHeader from "./BackHeader";

const AddElevatorForm = () => {
    const floors = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    const [currentFloor, setCurrentFloor] = useState('')
    const [destinationFloor, setDestinationFloor] = useState('')
    const navigate = useNavigate()

    useEffect(() => {
        setCurrentFloor('0')
        setDestinationFloor('0')
    }, [])

    const onSubmit = (e) => {
        e.preventDefault()
        Api.post("/api/v1/elevator", {
            currentFloor: currentFloor,
            destinationFloor: destinationFloor,
            floors: []
        }).then(res => {
            console.log(res.data)
            navigate("/simulation")
        }).catch((e) => {
            console.log(e)
        })
    }

    return (
        <div>
            <BackHeader />
            <Form
                className="w-50"
                style={{ marginLeft: 'auto', marginRight: 'auto', marginTop: '20px' }}
                onSubmit={onSubmit}
                id='form'
            >
                <Form.Group className="mb-3">
                    <Form.Label>Current floor</Form.Label>
                    <Form.Select
                        value={currentFloor}
                        onChange={(e) => setCurrentFloor(e.target.value)}
                    >
                        {floors.map((floor, index) => (
                            <option key={index}>{floor}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Destination floor</Form.Label>
                    <Form.Select
                        value={destinationFloor}
                        onChange={(e) => setDestinationFloor(e.target.value)}>
                        {floors.map((floor, index) => (
                            <option key={index}>{floor}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        </div>
    );
};

export default AddElevatorForm;