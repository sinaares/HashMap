import java.text.SimpleDateFormat;
import java.util.Date;
public class Customer
{
    private String customer_id;
    private String customer_name;
    private MultiLinkedList MarketHistori;
    public Customer(String id,String name)
    {
        this.customer_id=id;
        this.customer_name=name;
        MarketHistori=new MultiLinkedList();
    }
    //..................................................................................
    public void addSell(Date Date ,String product)
    {
        //this.MarketHistori.addParent(Date);
        this.MarketHistori.addChild(Date, product);
    }
    public String getId()
    {
        return this.customer_id;
    }
    @Override
    public String toString()
    {
        return this.customer_id+"\n"+this.customer_name+"\n" + MarketHistori.display();

    }

    public MultiLinkedList getmarket(){
        return this.MarketHistori;
    }


    public class MultiLinkedList {


        private parent head;

        public class parent{

            private Date parent;

            private parent down;
            private child right;

            public parent(Date data){
                parent = data;
                down = null;
                right = null;
            }
            public Date getParent(){
                return parent;
            }
            public void setParent(Date data){
                this.parent = data;
            }
            public parent getDown(){
                return down;
            }
            public void setDown(parent down){
                this.down = down;
            }
            public child getRight(){
                return right;
            }
            public void setright(child right){
                this.right = right;
            }
        }

        public class child{
            private String child;
            private child next;
            public child(String data){
                child = data;
                next = null;
            }
            public String getChild(){
                return child;
            }
            public void setChild(String data){
                this.child = data;
            }
            public child getNext(){
                return next;
            }
            public void setNext(child next){
                this.next = next;
            }

        }


        public void addChild(Date parentData, String childData) {
            parent existingParent = findParent(parentData);

            if (existingParent != null) {
                // Parent exists, add the child to the existing parent
                addChildToParent(existingParent, childData);
            } else {
                // Parent does not exist, add parent and child
                addParentSorted(parentData);
                parent newParent = findParent(parentData); // Find the newly added parent
                addChildToParent(newParent, childData);
            }
        }

        // Helper method to find a parent with the given date
        private parent findParent(Date parentData) {
            parent temp = head;

            while (temp != null) {
                if (parentData.equals(temp.getParent())) {
                    return temp; // Found the existing parent
                }
                temp = temp.getDown();
            }

            return null; // Parent with the given date does not exist
        }

        // Helper method to add a child to an existing parent
        private void addChildToParent(parent existingParent, String childData) {
            child tempChild = existingParent.getRight();
            child newChild = new child(childData);

            if (tempChild == null || childData.compareTo(tempChild.getChild()) < 0) {
                // Add the child at the beginning
                newChild.setNext(tempChild);
                existingParent.setright(newChild);
            } else {
                // Find the correct position to insert the child
                while (tempChild.getNext() != null && childData.compareTo(tempChild.getNext().getChild()) > 0) {
                    tempChild = tempChild.getNext();
                }
                newChild.setNext(tempChild.getNext());
                tempChild.setNext(newChild);
            }
            sortParents();
        }

        public void addParent(Date dataToAdd){
            if(head == null){
                parent newnode = new parent(dataToAdd);
                head = newnode;
            }
            else {
                parent temp = head;
                while (temp.getDown() != null){
                    temp = temp.getDown();
                }
                parent newnode = new parent(dataToAdd);
                temp.setDown(newnode);
            }
        }

        public void addParentSorted(Date dataToAdd) {
            parent newnode = new parent(dataToAdd);

            if (head == null || dataToAdd.compareTo(head.getParent()) < 0) {
                newnode.setDown(head);
                head = newnode;
            } else {
                parent temp = head;
                while (temp.getDown() != null && dataToAdd.compareTo(temp.getDown().getParent()) > 0) {
                    temp = temp.getDown();
                }
                newnode.setDown(temp.getDown());
                temp.setDown(newnode);
            }
        }
        public boolean isParentExists(Date searchDate) {
            parent temp = head;

            while (temp != null) {
                if (searchDate.equals(temp.getParent())) {
                    return true; // Parent with the given date exists
                }
                temp = temp.getDown();
            }

            return false; // Parent with the given date does not exist
        }

        public int sizzeCourse(){
            int count = 0;
            if(head == null){
                System.out.println("Linked list is empty");
            }
            else {
                parent temp = head;
                while (temp != null){
                    count++;
                    temp = temp.getDown();
                }
            }
            return count;
        }

        public String display() {
            StringBuilder result = new StringBuilder();
            parent temp = head;

            while (temp != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(temp.getParent());

                result.append(formattedDate).append(" -> ");

                child childTemp = temp.getRight();
                while (childTemp != null) {
                    result.append(childTemp.getChild()).append(" ");
                    childTemp = childTemp.getNext();
                }

                result.append("\n");
                temp = temp.getDown();
            }

            return result.toString();
        }

        public void sortParents() {
            if (head == null || head.getDown() == null) {
                return;
            }

            parent sortedHead = null;
            parent current = head;

            while (current != null) {
                parent next = current.getDown();
                sortedHead = insertIntoSorted(sortedHead, current);
                current = next;
            }

            head = sortedHead;
        }

        private parent insertIntoSorted(parent sortedHead, parent newNode) {
            if (sortedHead == null || newNode.getParent().compareTo(sortedHead.getParent()) >= 0 ) {
                newNode.setDown(sortedHead);
                return newNode;
            }

            parent current = sortedHead;
            while (current.getDown() != null && newNode.getParent().compareTo(current.getDown().getParent()) < 0 ) {
                current = current.getDown();
            }

            newNode.setDown(current.getDown());
            current.setDown(newNode);

            return sortedHead;
        }
    }
}
