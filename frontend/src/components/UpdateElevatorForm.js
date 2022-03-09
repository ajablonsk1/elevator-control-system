import React, {useState, useEffect} from 'react';
import {Button, Form} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import Api from "./Api";
import BackHeader from "./BackHeader";

const UpdateElevatorForm = () => {
    const floors = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    const [currentFloor, setCurrentFloor] = useState('')
    const [destinationFloor, setDestinationFloor] = useState('')
    const [id, setId] = useState('')
    const [elevators, setElevators] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        Api.get("/api/v1/elevators"
        ).then(res => {
            setElevators(res.data)
        }).catch((e) => {
            console.log(e)
        })
    }, [])

    useEffect(() => {
        console.log("XD")
        if(elevators.length > 0){
            setId(elevators[0].id)
        }
        setCurrentFloor('0')
        setDestinationFloor('0')
    }, [elevators])

    const onSubmit = (e) => {
        e.preventDefault()
        Api.put("/api/v1/elevator", {
            id: id,
            currentFloor: currentFloor,
            destinationFloor: destinationFloor
        }).then(res => {
            console.log(res.data)
            navigate("/simulation")
        }).catch(e => {
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
                    <Form.Label>Select elevator's id</Form.Label>
                    <Form.Select
                        value={id}
                        onChange={(e) => setId(e.target.value)}
                    >
                        {elevators.map((elevator, index) => (
                            <option key={index}>{elevator.id}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
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
                    Update
                </Button>
            </Form>
        </div>
    );
};

export default UpdateElevatorForm;