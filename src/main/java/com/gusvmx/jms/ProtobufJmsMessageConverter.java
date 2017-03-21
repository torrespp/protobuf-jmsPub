/**
 * 
 */
package com.gusvmx.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.google.protobuf.InvalidProtocolBufferException;
import com.gus.protobuf.model.MyModelProtos.Person;

/**
 * @author gus
 *
 */
public class ProtobufJmsMessageConverter implements MessageConverter {

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		BytesMessage bytesMessage = session.createBytesMessage();
		Person person = (Person) object;
		bytesMessage.writeBytes(person.toByteArray());
		return bytesMessage;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		BytesMessage bytesMessage = (BytesMessage) message;
		int messageLength = (int) bytesMessage.getBodyLength();
		byte[] rawPerson = new byte[messageLength];
		bytesMessage.readBytes(rawPerson);
		
		Person person = null;
		try {
			person = Person.parseFrom(rawPerson);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return person;
	}

}
